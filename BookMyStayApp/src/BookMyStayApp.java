import java.util.*;

// Custom Exception
class CancellationException extends Exception {
    public CancellationException(String message) {
        super(message);
    }
}

// Reservation Model
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getReservationId() { return reservationId; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }

    public boolean isCancelled() { return isCancelled; }

    public void cancel() {
        isCancelled = true;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId +
                ", Status: " + (isCancelled ? "Cancelled" : "Active");
    }
}

// Inventory Manager
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Standard", 1);
        inventory.put("Deluxe", 1);
    }

    public void allocate(String roomType) throws Exception {
        if (!inventory.containsKey(roomType) || inventory.get(roomType) <= 0) {
            throw new Exception("No rooms available for " + roomType);
        }
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void release(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void display() {
        System.out.println("\nInventory Status:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Booking History
class BookingHistory {
    private Map<String, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }

    public void displayAll() {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations.values()) {
            System.out.println(r);
        }
    }
}

// Cancellation Service with Stack (Rollback)
class CancellationService {
    private Stack<String> rollbackStack = new Stack<>();

    public void cancelBooking(String reservationId,
                              BookingHistory history,
                              RoomInventory inventory)
            throws CancellationException {

        Reservation res = history.getReservation(reservationId);

        // Validation
        if (res == null) {
            throw new CancellationException("Reservation not found.");
        }

        if (res.isCancelled()) {
            throw new CancellationException("Reservation already cancelled.");
        }

        // Step 1: Push roomId to stack (LIFO rollback tracking)
        rollbackStack.push(res.getRoomId());

        // Step 2: Restore inventory
        inventory.release(res.getRoomType());

        // Step 3: Mark reservation cancelled
        res.cancel();

        System.out.println("\nCancellation successful for Reservation ID: " + reservationId);
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (Recent Releases): " + rollbackStack);
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService();

        try {
            // Simulate confirmed bookings
            inventory.allocate("Standard");
            Reservation r1 = new Reservation("RES301", "Arun", "Standard", "S1");
            history.addReservation(r1);

            inventory.allocate("Deluxe");
            Reservation r2 = new Reservation("RES302", "Priya", "Deluxe", "D1");
            history.addReservation(r2);

        } catch (Exception e) {
            System.out.println("Error during booking setup: " + e.getMessage());
        }

        // Initial state
        history.displayAll();
        inventory.display();

        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter Reservation ID to cancel: ");
        String inputId = scanner.nextLine();

        try {
            cancellationService.cancelBooking(inputId, history, inventory);
        } catch (CancellationException e) {
            System.out.println("\nCancellation Failed: " + e.getMessage());
        }

        // Final state
        history.displayAll();
        inventory.display();
        cancellationService.displayRollbackStack();

        scanner.close();
    }
}
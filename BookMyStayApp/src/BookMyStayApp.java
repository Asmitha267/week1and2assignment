import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Represents a Reservation
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType;
    }
}

// Manages Room Inventory
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public boolean isAvailable(String roomType) {
        return inventory.get(roomType) > 0;
    }

    public void allocateRoom(String roomType) throws InvalidBookingException {
        int count = inventory.get(roomType);

        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        inventory.put(roomType, count - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Validator Class
class InvalidBookingValidator {

    public static void validate(String reservationId, String guestName,
                                String roomType, RoomInventory inventory)
            throws InvalidBookingException {

        if (reservationId == null || reservationId.trim().isEmpty()) {
            throw new InvalidBookingException("Reservation ID cannot be empty.");
        }

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidBookingException("Invalid room type selected: " + roomType);
        }

        if (!inventory.isAvailable(roomType)) {
            throw new InvalidBookingException("Selected room type is fully booked: " + roomType);
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        RoomInventory inventory = new RoomInventory();

        System.out.println("=== Hotel Booking System (Validation Demo) ===");

        try {
            // Input from user
            System.out.print("Enter Reservation ID: ");
            String reservationId = scanner.nextLine();

            System.out.print("Enter Guest Name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter Room Type (Standard/Deluxe/Suite): ");
            String roomType = scanner.nextLine();

            // Validation (Fail-Fast)
            InvalidBookingValidator.validate(reservationId, guestName, roomType, inventory);

            // Allocation (only if valid)
            inventory.allocateRoom(roomType);

            // Create reservation
            Reservation reservation = new Reservation(reservationId, guestName, roomType);

            System.out.println("\nBooking Confirmed!");
            System.out.println(reservation);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("\nBooking Failed: " + e.getMessage());
        } catch (Exception e) {
            // Catch unexpected errors
            System.out.println("\nUnexpected Error: " + e.getMessage());
        }

        // System continues running safely
        inventory.displayInventory();

        scanner.close();
    }
}
import java.util.*;

/**
 * Reservation
 * Represents a booking request from a guest.
 *
 * @version 6.0
 */
class Reservation {

    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

/**
 * RoomInventory
 * Maintains room availability using HashMap.
 *
 * @version 6.0
 */
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single", 2);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        int count = inventory.get(roomType);
        inventory.put(roomType, count - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Rooms Remaining: " + entry.getValue());
        }
    }
}

/**
 * BookingService
 * Processes reservation queue and allocates rooms safely.
 *
 * @version 6.0
 */
class BookingService {

    private Queue<Reservation> bookingQueue;
    private RoomInventory inventory;

    private Set<String> allocatedRoomIds;
    private HashMap<String, Set<String>> allocatedRoomsByType;

    private int roomCounter = 1;

    public BookingService(RoomInventory inventory) {

        this.inventory = inventory;
        bookingQueue = new LinkedList<>();

        allocatedRoomIds = new HashSet<>();
        allocatedRoomsByType = new HashMap<>();
    }

    public void addRequest(Reservation reservation) {
        bookingQueue.add(reservation);
    }

    /**
     * Process booking queue in FIFO order
     */
    public void processBookings() {

        System.out.println("\nProcessing Booking Requests...\n");

        while (!bookingQueue.isEmpty()) {

            Reservation reservation = bookingQueue.poll();
            String roomType = reservation.roomType;

            if (inventory.getAvailability(roomType) > 0) {

                String roomId = roomType.substring(0,1).toUpperCase() + roomCounter++;

                // ensure uniqueness
                allocatedRoomIds.add(roomId);

                allocatedRoomsByType
                        .computeIfAbsent(roomType, k -> new HashSet<>())
                        .add(roomId);

                inventory.decrementRoom(roomType);

                System.out.println("Reservation Confirmed");
                System.out.println("Guest: " + reservation.guestName);
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);
                System.out.println("---------------------------");

            } else {

                System.out.println("Reservation Failed (No Availability)");
                System.out.println("Guest: " + reservation.guestName);
                System.out.println("Room Type: " + roomType);
                System.out.println("---------------------------");
            }
        }
    }
}

/**
 * UseCase6RoomAllocationService
 * Demonstrates safe reservation confirmation and room allocation.
 *
 * @version 6.1
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Hotel Booking System v6.1");

        RoomInventory inventory = new RoomInventory();

        BookingService bookingService = new BookingService(inventory);

        // Booking requests
        bookingService.addRequest(new Reservation("Alice", "Single"));
        bookingService.addRequest(new Reservation("Bob", "Double"));
        bookingService.addRequest(new Reservation("Charlie", "Single"));
        bookingService.addRequest(new Reservation("David", "Suite"));

        bookingService.processBookings();

        inventory.displayInventory();
    }
}
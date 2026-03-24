import java.util.*;

// Booking Request Model
class BookingRequest {
    private String guestName;
    private String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// Thread-safe Room Inventory
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
    }

    // Critical Section (Synchronized)
    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            // Simulate delay (to expose race condition if not synchronized)
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
        notify(); // notify waiting threads
    }

    public synchronized BookingRequest getRequest() {
        while (queue.isEmpty()) {
            try {
                wait(); // wait until request arrives
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return queue.poll();
    }
}

// Booking Processor (Thread)
class BookingProcessor implements Runnable {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request = queue.getRequest();

            if (request == null) continue;

            boolean success = inventory.allocateRoom(request.getRoomType());

            if (success) {
                System.out.println(Thread.currentThread().getName() +
                        " SUCCESS: Room booked for " + request.getGuestName() +
                        " (" + request.getRoomType() + ")");
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " FAILED: No rooms available for " +
                        request.getGuestName() +
                        " (" + request.getRoomType() + ")");
            }

            // Exit condition (for demo)
            if (Thread.currentThread().isInterrupted()) break;
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Create worker threads
        Thread t1 = new Thread(new BookingProcessor(queue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(queue, inventory), "Thread-2");

        t1.start();
        t2.start();

        // Simulate concurrent booking requests
        queue.addRequest(new BookingRequest("Arun", "Standard"));
        queue.addRequest(new BookingRequest("Priya", "Standard"));
        queue.addRequest(new BookingRequest("Karthik", "Standard")); // should fail (only 2)
        queue.addRequest(new BookingRequest("Meena", "Deluxe"));
        queue.addRequest(new BookingRequest("Ravi", "Deluxe")); // should fail (only 1)

        // Allow threads to process
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}

        // Stop threads (gracefully for demo)
        t1.interrupt();
        t2.interrupt();

        // Display final inventory
        inventory.displayInventory();
    }
}
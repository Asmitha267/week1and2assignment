import java.util.LinkedList;
import java.util.Queue;

/**
 * Reservation
 * Represents a guest booking request.
 *
 * @version 5.0
 */
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

/**
 * BookingRequestQueue
 * Manages incoming booking requests using FIFO queue.
 *
 * @version 5.0
 */
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    /**
     * Add reservation request to queue
     */
    public void addRequest(Reservation reservation) {
        queue.add(reservation);
        System.out.println("Booking request added to queue.");
    }

    /**
     * Display all queued requests
     */
    public void displayRequests() {

        System.out.println("\nCurrent Booking Request Queue:");

        for (Reservation r : queue) {
            r.displayReservation();
        }
    }
}

/**
 * UseCase5BookingRequestQueue
 * Demonstrates booking request intake using FIFO queue.
 *
 * @version 5.1
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Hotel Booking System v5.1\n");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Guest booking requests
        Reservation r1 = new Reservation("Alice", "Single");
        Reservation r2 = new Reservation("Bob", "Double");
        Reservation r3 = new Reservation("Charlie", "Suite");

        // Add requests to queue (FIFO order)
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Display queued requests
        bookingQueue.displayRequests();

        System.out.println("\nRequests stored in arrival order. Awaiting processing.");
    }
}
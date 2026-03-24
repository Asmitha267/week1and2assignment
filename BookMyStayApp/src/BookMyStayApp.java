import java.io.*;
import java.util.*;

// Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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

// Wrapper class to persist full system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    List<Reservation> reservations;
    Map<String, Integer> inventory;

    public SystemState(List<Reservation> reservations, Map<String, Integer> inventory) {
        this.reservations = reservations;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "hotel_state.ser";

    // Save state to file
    public void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("\nSystem state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state from file
    public SystemState load() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No previous state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("System state loaded successfully.");
            return (SystemState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state (corrupt file). Starting fresh.");
            return null;
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        PersistenceService persistenceService = new PersistenceService();

        // Attempt recovery
        SystemState state = persistenceService.load();

        List<Reservation> reservations;
        Map<String, Integer> inventory;

        if (state != null) {
            // Restore previous state
            reservations = state.reservations;
            inventory = state.inventory;
        } else {
            // Initialize fresh state
            reservations = new ArrayList<>();
            inventory = new HashMap<>();
            inventory.put("Standard", 2);
            inventory.put("Deluxe", 1);
        }

        // Simulate system usage
        System.out.println("\n--- Current System State ---");

        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation r : reservations) {
                System.out.println(r);
            }
        }

        System.out.println("\nInventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Simulate adding new booking
        System.out.println("\nAdding new booking...");
        Reservation newRes = new Reservation("RES401", "Arun", "Standard");
        reservations.add(newRes);
        inventory.put("Standard", inventory.get("Standard") - 1);

        System.out.println("Booking added: " + newRes);

        // Save state before shutdown
        SystemState newState = new SystemState(reservations, inventory);
        persistenceService.save(newState);

        System.out.println("\nRestart the program to see recovery in action.");
    }
}
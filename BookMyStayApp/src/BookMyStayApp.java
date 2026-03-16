import java.util.HashMap;
import java.util.Map;

/**
 * RoomInventory
 *
 * Manages centralized room availability using HashMap.
 * This replaces scattered variables with a single source of truth.
 *
 * @version 3.0
 */
class RoomInventory {

    private HashMap<String, Integer> inventory;

    /**
     * Constructor initializes room inventory
     */
    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single", 10);
        inventory.put("Double", 5);
        inventory.put("Suite", 2);
    }

    /**
     * Get availability of a room type
     */
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    /**
     * Update availability of a room type
     */
    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    /**
     * Display complete inventory
     */
    public void displayInventory() {
        System.out.println("\nCurrent Room Inventory:");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Rooms Available: " + entry.getValue());
        }
    }
}

/**
 * UseCase3InventorySetup
 * Demonstrates centralized room inventory management.
 *
 * @version 3.1
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Hotel Booking System v3.1");

        RoomInventory inventory = new RoomInventory();

        inventory.displayInventory();

        System.out.println("\nChecking availability for Double rooms:");
        System.out.println("Available: " + inventory.getAvailability("Double"));

        System.out.println("\nUpdating Double room availability...");
        inventory.updateAvailability("Double", 4);

        inventory.displayInventory();
    }
}
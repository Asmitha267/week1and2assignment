import java.util.HashMap;
import java.util.Map;

/**
 * Room
 * Represents room details in the system.
 *
 * @version 4.0
 */
class Room {

    private String type;
    private int price;
    private String amenities;

    public Room(String type, int price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: $" + price);
        System.out.println("Amenities: " + amenities);
    }
}

/**
 * RoomInventory
 * Maintains centralized availability state.
 *
 * @version 4.0
 */
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 0); // Example unavailable room
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }
}

/**
 * UseCase4RoomSearch
 * Demonstrates read-only room search functionality.
 *
 * @version 4.1
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Hotel Booking System v4.1\n");

        RoomInventory inventory = new RoomInventory();

        Room single = new Room("Single", 100, "WiFi, TV, AC");
        Room doubleRoom = new Room("Double", 180, "WiFi, TV, AC, Mini Bar");
        Room suite = new Room("Suite", 350, "WiFi, TV, AC, Mini Bar, Jacuzzi");

        Room[] rooms = {single, doubleRoom, suite};

        System.out.println("Available Rooms:\n");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getType());

            // Defensive check: only show rooms with availability > 0
            if (available > 0) {

                room.displayDetails();
                System.out.println("Available Count: " + available);
                System.out.println("---------------------------");
            }
        }

        System.out.println("\nSearch completed. (Inventory unchanged)");
    }
}
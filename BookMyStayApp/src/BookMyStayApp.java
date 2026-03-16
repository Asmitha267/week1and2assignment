/**
 * UseCase2RoomInitialization
 * Initializes room inventory for Book My Stay application
 *
 * @version 2.0
 */

class Room {

    int roomNumber;
    String roomType;
    boolean isAvailable;

    Room(int roomNumber, String roomType, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.isAvailable = isAvailable;
    }

    void displayRoom() {
        System.out.println("Room Number: " + roomNumber);
        System.out.println("Room Type: " + roomType);
        System.out.println("Available: " + isAvailable);
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Hotel Booking System v2.0");

        Room[] rooms = new Room[3];

        rooms[0] = new Room(101, "Single", true);
        rooms[1] = new Room(102, "Double", true);
        rooms[2] = new Room(103, "Suite", false);

        System.out.println("\nRoom Inventory:");

        for (Room room : rooms) {
            room.displayRoom();
            System.out.println();
        }
    }
}
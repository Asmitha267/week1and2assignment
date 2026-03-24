import java.util.*;

// Represents an Add-On Service
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Manages mapping between Reservation and Add-On Services
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<AddOnService>> reservationServicesMap;

    public AddOnServiceManager() {
        reservationServicesMap = new HashMap<>();
    }

    // Add services to a reservation
    public void addServices(String reservationId, List<AddOnService> services) {
        reservationServicesMap.putIfAbsent(reservationId, new ArrayList<>());
        reservationServicesMap.get(reservationId).addAll(services);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost of services
    public double calculateTotalServiceCost(String reservationId) {
        double total = 0.0;
        List<AddOnService> services = getServices(reservationId);

        for (AddOnService service : services) {
            total += service.getCost();
        }

        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Selected Add-On Services:");
        for (AddOnService service : services) {
            System.out.println("- " + service);
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AddOnServiceManager manager = new AddOnServiceManager();

        // Sample Reservation ID (existing booking)
        String reservationId = "RES123";

        // Available services
        List<AddOnService> availableServices = Arrays.asList(
                new AddOnService("Breakfast", 500),
                new AddOnService("Airport Pickup", 1200),
                new AddOnService("Extra Bed", 800),
                new AddOnService("Spa Access", 1500)
        );

        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Available Add-On Services:");

        for (int i = 0; i < availableServices.size(); i++) {
            System.out.println((i + 1) + ". " + availableServices.get(i));
        }

        System.out.println("\nEnter service numbers to add (comma-separated): ");
        String input = scanner.nextLine();

        String[] choices = input.split(",");
        List<AddOnService> selectedServices = new ArrayList<>();

        for (String choice : choices) {
            try {
                int index = Integer.parseInt(choice.trim()) - 1;
                if (index >= 0 && index < availableServices.size()) {
                    selectedServices.add(availableServices.get(index));
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input ignored: " + choice);
            }
        }

        // Add selected services to reservation
        manager.addServices(reservationId, selectedServices);

        // Display selected services
        System.out.println("\n--- Add-On Summary ---");
        manager.displayServices(reservationId);

        // Calculate total cost
        double totalCost = manager.calculateTotalServiceCost(reservationId);
        System.out.println("Total Add-On Cost: ₹" + totalCost);

        scanner.close();
    }
}
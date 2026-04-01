import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;

class Vehicle {
    String licensePlate;
    LocalDateTime entryTime;

    Vehicle(String licensePlate) {
        this.licensePlate = licensePlate;
        this.entryTime = LocalDateTime.now();
    }
}

class ParkingLot {
    private Vehicle[] spots;
    private int capacity;
    private int occupied = 0;
    private int totalProbes = 0;
    private Map<Integer, Integer> peakHourMap = new HashMap<>(); // Hour -> occupancy

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        spots = new Vehicle[capacity];
    }

    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    // Park vehicle with linear probing
    public int parkVehicle(String licensePlate) {
        int preferred = hash(licensePlate);
        int probes = 0;
        for (int i = 0; i < capacity; i++) {
            int idx = (preferred + i) % capacity;
            if (spots[idx] == null) {
                spots[idx] = new Vehicle(licensePlate);
                occupied++;
                totalProbes += probes;
                int hour = LocalDateTime.now().getHour();
                peakHourMap.put(hour, peakHourMap.getOrDefault(hour, 0) + 1);
                System.out.println("Assigned Spot #" + idx + " (" + probes + " probes)");
                return idx;
            }
            probes++;
        }
        System.out.println("Parking full! Cannot assign spot.");
        return -1;
    }

    // Exit vehicle and calculate billing
    public void exitVehicle(String licensePlate, double ratePerHour) {
        for (int i = 0; i < capacity; i++) {
            if (spots[i] != null && spots[i].licensePlate.equals(licensePlate)) {
                Vehicle v = spots[i];
                Duration duration = Duration.between(v.entryTime, LocalDateTime.now());
                double hours = duration.toMinutes() / 60.0;
                double fee = hours * ratePerHour;
                spots[i] = null;
                occupied--;
                System.out.printf("Spot #%d freed, Duration: %.2f hrs, Fee: $%.2f%n", i, hours, fee);
                return;
            }
        }
        System.out.println("Vehicle not found!");
    }

    // Statistics
    public void getStatistics() {
        double avgProbes = occupied == 0 ? 0 : (double) totalProbes / occupied;
        int peakHour = peakHourMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(-1);
        double occupancy = ((double) occupied / capacity) * 100;
        System.out.printf("Occupancy: %.2f%%, Avg Probes: %.2f, Peak Hour: %d%n", occupancy, avgProbes, peakHour);
    }
}

public class ParkingLotApp {
    public static void main(String[] args) throws InterruptedException {
        ParkingLot lot = new ParkingLot(10); // small for demo

        lot.parkVehicle("ABC-1234");
        Thread.sleep(1000);
        lot.parkVehicle("ABC-1235");
        Thread.sleep(1000);
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234", 5.0);
        lot.getStatistics();
    }
}
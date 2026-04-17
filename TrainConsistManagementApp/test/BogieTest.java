import java.util.*;
import java.util.stream.Collectors;

public class TrainConsistManagementApp {

    public static void main(String[] args) {

        // Step 1: Create list of bogies
        List<Bogie> bogies = new ArrayList<>();
        bogies.add(new Bogie("Sleeper"));
        bogies.add(new Bogie("AC Chair"));
        bogies.add(new Bogie("Sleeper"));
        bogies.add(new Bogie("First Class"));
        bogies.add(new Bogie("AC Chair"));

        // Step 2: Convert to stream and group
        Map<String, List<Bogie>> groupedBogies =
                bogies.stream()
                        .collect(Collectors.groupingBy(Bogie::getType));

        // Step 3: Display grouped result
        System.out.println("Grouped Bogies:");
        groupedBogies.forEach((type, list) -> {
            System.out.println(type + " -> " + list);
        });

        // Step 4: Verify original list unchanged
        System.out.println("\nOriginal Bogie List:");
        bogies.forEach(System.out::println);
    }
}
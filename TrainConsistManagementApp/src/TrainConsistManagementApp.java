import java.util.*;
import java.util.stream.Collectors;

// Main class
public class TrainConsistManagementApp {

    public static void main(String[] args) {

        List<Bogie> bogies = new ArrayList<>();

        bogies.add(new Bogie("B1", "Sleeper"));
        bogies.add(new Bogie("B2", "AC Chair"));
        bogies.add(new Bogie("B3", "Sleeper"));
        bogies.add(new Bogie("B4", "First Class"));
        bogies.add(new Bogie("B5", "AC Chair"));
        bogies.add(new Bogie("B6", "Rectangular"));

        Map<String, List<Bogie>> groupedBogies =
                bogies.stream()
                        .collect(Collectors.groupingBy(Bogie::getType));

        System.out.println("=== Grouped Bogies by Type ===\n");

        groupedBogies.forEach((type, list) -> {
            System.out.println("Type: " + type);
            list.forEach(System.out::println);
            System.out.println();
        });
    }
}

// Bogie class (same file, non-public)
class Bogie {
    private String bogieId;
    private String type;

    public Bogie(String bogieId, String type) {
        this.bogieId = bogieId;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getBogieId() {
        return bogieId;
    }

    @Override
    public String toString() {
        return "[" + bogieId + " - " + type + "]";
    }
}
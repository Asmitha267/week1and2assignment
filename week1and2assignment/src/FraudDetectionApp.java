import java.time.LocalDateTime;
import java.util.*;

class Transaction {
    int id;
    double amount;
    String merchant;
    String account;
    LocalDateTime timestamp;

    Transaction(int id, double amount, String merchant, String account, LocalDateTime timestamp) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("(id:%d, amount:%.2f, merchant:%s, account:%s)", id, amount, merchant, account);
    }
}

class FraudDetector {
    List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Classic Two-Sum
    public List<int[]> findTwoSum(double target) {
        Map<Double, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();
        for (Transaction t : transactions) {
            double complement = target - t.amount;
            if (map.containsKey(complement)) {
                result.add(new int[]{map.get(complement).id, t.id});
            }
            map.put(t.amount, t);
        }
        return result;
    }

    // Detect duplicates: same amount + merchant but different accounts
    public List<String> detectDuplicates() {
        Map<String, Map<Double, Set<String>>> map = new HashMap<>();
        List<String> duplicates = new ArrayList<>();

        for (Transaction t : transactions) {
            map.putIfAbsent(t.merchant, new HashMap<>());
            Map<Double, Set<String>> merchantMap = map.get(t.merchant);
            merchantMap.putIfAbsent(t.amount, new HashSet<>());
            Set<String> accounts = merchantMap.get(t.amount);

            if (!accounts.isEmpty() && !accounts.contains(t.account)) {
                duplicates.add(String.format("Duplicate detected: merchant=%s, amount=%.2f, accounts=%s",
                        t.merchant, t.amount, accounts + ", " + t.account));
            }
            accounts.add(t.account);
        }

        return duplicates;
    }

    // K-Sum (simple recursive for demonstration; can be optimized with memoization)
    public List<List<Transaction>> findKSum(int k, double target) {
        List<List<Transaction>> results = new ArrayList<>();
        kSumHelper(transactions, k, target, 0, new ArrayList<>(), results);
        return results;
    }

    private void kSumHelper(List<Transaction> txs, int k, double target, int index,
                            List<Transaction> path, List<List<Transaction>> results) {
        if (k == 0 && Math.abs(target) < 0.0001) { // floating point comparison
            results.add(new ArrayList<>(path));
            return;
        }
        if (k == 0 || index >= txs.size()) return;

        // Include current transaction
        path.add(txs.get(index));
        kSumHelper(txs, k - 1, target - txs.get(index).amount, index + 1, path, results);
        path.remove(path.size() - 1);

        // Exclude current transaction
        kSumHelper(txs, k, target, index + 1, path, results);
    }
}

public class FraudDetectionApp {
    public static void main(String[] args) {
        FraudDetector detector = new FraudDetector();

        detector.addTransaction(new Transaction(1, 500, "Store A", "acc1", LocalDateTime.now()));
        detector.addTransaction(new Transaction(2, 300, "Store B", "acc2", LocalDateTime.now()));
        detector.addTransaction(new Transaction(3, 200, "Store C", "acc3", LocalDateTime.now()));
        detector.addTransaction(new Transaction(4, 500, "Store A", "acc2", LocalDateTime.now()));

        // Classic Two-Sum
        System.out.println("Two-Sum (target=500): " + detector.findTwoSum(500));

        // Detect duplicates
        System.out.println("Duplicates:");
        detector.detectDuplicates().forEach(System.out::println);

        // K-Sum (example: k=3, target=1000)
        System.out.println("K-Sum (k=3, target=1000):");
        List<List<Transaction>> kSumResults = detector.findKSum(3, 1000);
        for (List<Transaction> combo : kSumResults) {
            System.out.println(combo);
        }
    }
}
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FlashSaleManager {

    // Product stock: productId -> stockCount
    private Map<String, AtomicInteger> stockMap = new ConcurrentHashMap<>();

    // Waiting list: productId -> queue of userIds
    private Map<String, LinkedList<Integer>> waitingList = new ConcurrentHashMap<>();

    // Initialize stock for a product
    public void addProduct(String productId, int quantity) {
        stockMap.put(productId, new AtomicInteger(quantity));
        waitingList.put(productId, new LinkedList<>());
    }

    // Check stock for a product
    public int checkStock(String productId) {
        AtomicInteger stock = stockMap.get(productId);
        return stock != null ? stock.get() : 0;
    }

    // Attempt to purchase a product
    public synchronized String purchaseItem(String productId, int userId) {
        AtomicInteger stock = stockMap.get(productId);
        if (stock == null) return "Product does not exist";

        if (stock.get() > 0) {
            stock.decrementAndGet(); // Safe decrement
            return "Success, " + stock.get() + " units remaining";
        } else {
            // Add to waiting list
            LinkedList<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            return "Added to waiting list, position #" + queue.size();
        }
    }

    // Serve waiting list when stock is replenished
    public synchronized void restock(String productId, int quantity) {
        AtomicInteger stock = stockMap.get(productId);
        LinkedList<Integer> queue = waitingList.get(productId);
        if (stock == null || queue == null) return;

        stock.addAndGet(quantity);

        while (!queue.isEmpty() && stock.get() > 0) {
            int userId = queue.poll(); // FIFO
            stock.decrementAndGet();
            System.out.println("Notified user " + userId + ": product now available!");
        }
    }

    public static void main(String[] args) {
        FlashSaleManager manager = new FlashSaleManager();

        // Add a product with 100 units
        manager.addProduct("IPHONE15_256GB", 100);

        // Simulate purchases
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        // Simulate stock depletion
        for (int i = 0; i < 100; i++) {
            manager.purchaseItem("IPHONE15_256GB", 10000 + i);
        }

        // Purchase after stock depleted
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));

        // Restock
        manager.restock("IPHONE15_256GB", 5);
    }
}
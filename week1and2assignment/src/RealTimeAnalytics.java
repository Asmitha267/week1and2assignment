import java.util.*;

class PageViewEvent {
    String url;
    String userId;
    String source;

    public PageViewEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class RealTimeAnalytics {

    // Counts total visits per page
    private Map<String, Integer> pageViews = new HashMap<>();
    // Tracks unique visitors per page
    private Map<String, Set<String>> uniqueVisitors = new HashMap<>();
    // Counts visits by traffic source
    private Map<String, Integer> trafficSources = new HashMap<>();

    // Process a page view event
    public void processEvent(PageViewEvent event) {
        pageViews.put(event.url, pageViews.getOrDefault(event.url, 0) + 1);
        uniqueVisitors.computeIfAbsent(event.url, k -> new HashSet<>()).add(event.userId);
        trafficSources.put(event.source, trafficSources.getOrDefault(event.source, 0) + 1);
    }

    // Display dashboard
    public void displayDashboard() {
        System.out.println("Top Pages:");
        // Sort pages by total visits descending
        pageViews.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .limit(10)
                .forEach(entry -> {
                    String url = entry.getKey();
                    int visits = entry.getValue();
                    int unique = uniqueVisitors.getOrDefault(url, Collections.emptySet()).size();
                    System.out.println(url + " - " + visits + " views (" + unique + " unique)");
                });

        System.out.println("\nTraffic Sources:");
        int total = trafficSources.values().stream().mapToInt(Integer::intValue).sum();
        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {
            double percentage = (entry.getValue() * 100.0) / total;
            System.out.println(entry.getKey() + ": " + String.format("%.1f", percentage) + "%");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        RealTimeAnalytics analytics = new RealTimeAnalytics();

        // Simulate some events
        analytics.processEvent(new PageViewEvent("/article/breaking-news", "user_123", "Google"));
        analytics.processEvent(new PageViewEvent("/article/breaking-news", "user_456", "Facebook"));
        analytics.processEvent(new PageViewEvent("/sports/championship", "user_789", "Direct"));
        analytics.processEvent(new PageViewEvent("/article/breaking-news", "user_123", "Google")); // repeated user

        // Display dashboard
        analytics.displayDashboard();
    }
}
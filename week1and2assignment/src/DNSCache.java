import java.util.*;
import java.util.concurrent.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime; // in milliseconds

    public DNSEntry(String domain, String ipAddress, long ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class DNSCache {
    private final int capacity;
    private Map<String, DNSEntry> cache;
    private int hits = 0;
    private int misses = 0;

    public DNSCache(int capacity) {
        this.capacity = capacity;
        // LRU Cache using access-order LinkedHashMap
        cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNSCache.this.capacity;
            }
        };

        // Background thread to clean expired entries
        ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();
        cleaner.scheduleAtFixedRate(this::removeExpiredEntries, 5, 5, TimeUnit.SECONDS);
    }

    // Resolve domain
    public synchronized String resolve(String domain) {
        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            return "Cache HIT → " + entry.ipAddress;
        }

        misses++;
        // Simulate upstream DNS query
        String ip = queryUpstreamDNS(domain);
        long ttl = 300; // default TTL 300s
        cache.put(domain, new DNSEntry(domain, ip, ttl));

        return "Cache MISS → Queried upstream → " + ip + " (TTL: " + ttl + "s)";
    }

    // Simulate upstream DNS query
    private String queryUpstreamDNS(String domain) {
        // For simplicity, return fake IP
        return "192.168.1." + new Random().nextInt(255);
    }

    // Remove expired entries
    private synchronized void removeExpiredEntries() {
        Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, DNSEntry> e = it.next();
            if (e.getValue().isExpired()) {
                it.remove();
            }
        }
    }

    public synchronized void getCacheStats() {
        int total = hits + misses;
        double hitRate = total > 0 ? (hits * 100.0 / total) : 0;
        System.out.println("Cache Stats → Hit Rate: " + hitRate + "%, Hits: " + hits + ", Misses: " + misses);
    }

    public static void main(String[] args) throws InterruptedException {
        DNSCache dnsCache = new DNSCache(5);

        System.out.println(dnsCache.resolve("google.com"));
        System.out.println(dnsCache.resolve("github.com"));
        System.out.println(dnsCache.resolve("google.com")); // Should be HIT

        Thread.sleep(310 * 1000); // wait 310 seconds for TTL expiry

        System.out.println(dnsCache.resolve("google.com")); // Should MISS again

        dnsCache.getCacheStats();
    }
}
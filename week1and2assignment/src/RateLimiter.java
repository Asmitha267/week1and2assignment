import java.util.concurrent.ConcurrentHashMap;

class TokenBucket {
    private int maxTokens;
    private int tokens;
    private long lastRefillTime;
    private int refillRate; // tokens per second

    public TokenBucket(int maxTokens, int refillRate) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // Try to consume a token
    public synchronized boolean tryConsume() {
        refillTokens();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    // Refill tokens based on elapsed time
    private void refillTokens() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTime; // in ms
        int refill = (int)(elapsed / 1000) * refillRate;
        if (refill > 0) {
            tokens = Math.min(maxTokens, tokens + refill);
            lastRefillTime = now;
        }
    }

    public int getRemainingTokens() {
        refillTokens();
        return tokens;
    }
}

public class RateLimiter {
    private ConcurrentHashMap<String, TokenBucket> clients = new ConcurrentHashMap<>();
    private int maxTokens = 1000; // per hour
    private int refillRate = 1;   // tokens per second (approx for 3600 tokens/hour)

    // Check if client can make request
    public boolean checkRateLimit(String clientId) {
        clients.putIfAbsent(clientId, new TokenBucket(maxTokens, refillRate));
        TokenBucket bucket = clients.get(clientId);
        return bucket.tryConsume();
    }

    // Get status for client
    public String getRateLimitStatus(String clientId) {
        TokenBucket bucket = clients.get(clientId);
        if (bucket == null) return "Client not found";
        return "Remaining tokens: " + bucket.getRemainingTokens();
    }

    public static void main(String[] args) {
        RateLimiter limiter = new RateLimiter();
        String clientId = "abc123";

        // Simulate requests
        for (int i = 0; i < 5; i++) {
            boolean allowed = limiter.checkRateLimit(clientId);
            System.out.println("Request " + (i+1) + " allowed: " + allowed);
        }

        System.out.println(limiter.getRateLimitStatus(clientId));
    }
}
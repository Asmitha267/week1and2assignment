import java.util.*;

class VideoData {
    String videoId;
    String title;
    int duration; // in seconds

    VideoData(String videoId, String title, int duration) {
        this.videoId = videoId;
        this.title = title;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", videoId, title);
    }
}

class MultiLevelCache {
    private LinkedHashMap<String, VideoData> L1Cache;
    private HashMap<String, VideoData> L2Cache;
    private HashMap<String, VideoData> L3Database;

    private int L1Capacity = 10000;
    private int L2Capacity = 100000;

    private Map<String, Integer> L1AccessCount;
    private int L1Hits = 0, L2Hits = 0, L3Hits = 0, totalRequests = 0;

    public MultiLevelCache() {
        L1Cache = new LinkedHashMap<>(L1Capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L1Capacity;
            }
        };
        L2Cache = new HashMap<>();
        L3Database = new HashMap<>();
        L1AccessCount = new HashMap<>();
    }

    // Add video to database
    public void addVideoToDB(VideoData video) {
        L3Database.put(video.videoId, video);
    }

    // Retrieve video
    public VideoData getVideo(String videoId) {
        totalRequests++;
        // Check L1
        if (L1Cache.containsKey(videoId)) {
            L1Hits++;
            incrementAccess(videoId);
            return L1Cache.get(videoId);
        }

        // Check L2
        if (L2Cache.containsKey(videoId)) {
            L2Hits++;
            VideoData video = L2Cache.get(videoId);
            promoteToL1(video);
            return video;
        }

        // Fetch from L3
        if (L3Database.containsKey(videoId)) {
            L3Hits++;
            VideoData video = L3Database.get(videoId);
            addToL2(video);
            return video;
        }

        return null; // Video not found
    }

    private void incrementAccess(String videoId) {
        L1AccessCount.put(videoId, L1AccessCount.getOrDefault(videoId, 0) + 1);
    }

    private void promoteToL1(VideoData video) {
        L1Cache.put(video.videoId, video);
        incrementAccess(video.videoId);
    }

    private void addToL2(VideoData video) {
        if (L2Cache.size() >= L2Capacity) {
            // Simple eviction: remove random entry for demonstration
            Iterator<String> it = L2Cache.keySet().iterator();
            if (it.hasNext()) it.next(); it.remove();
        }
        L2Cache.put(video.videoId, video);
    }

    public void printStatistics() {
        System.out.println("Cache Statistics:");
        System.out.printf("Total Requests: %d\n", totalRequests);
        System.out.printf("L1 Hit Rate: %.2f%%\n", 100.0 * L1Hits / totalRequests);
        System.out.printf("L2 Hit Rate: %.2f%%\n", 100.0 * L2Hits / totalRequests);
        System.out.printf("L3 Hit Rate: %.2f%%\n", 100.0 * L3Hits / totalRequests);
    }
}

public class MultiLevelCacheApp {
    public static void main(String[] args) {
        MultiLevelCache cache = new MultiLevelCache();

        // Populate database
        for (int i = 1; i <= 100000; i++) {
            cache.addVideoToDB(new VideoData("video_" + i, "Title " + i, 300 + i));
        }

        // Simulate video requests
        cache.getVideo("video_1");   // L3 → L2 → L1
        cache.getVideo("video_2");   // L3 → L2 → L1
        cache.getVideo("video_1");   // L1 hit
        cache.getVideo("video_3");   // L3 → L2 → L1
        cache.getVideo("video_2");   // L1 hit

        cache.printStatistics();
    }
}
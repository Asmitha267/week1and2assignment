import java.util.*;

public class PlagiarismDetector {

    private Map<String, Set<String>> ngramIndex = new HashMap<>();
    private int N = 5; // Use 5-grams

    // Process a document: break into n-grams and index
    public void indexDocument(String docId, String content) {
        String[] words = content.split("\\s+");
        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }
            String ngram = sb.toString().trim();
            ngramIndex.computeIfAbsent(ngram, k -> new HashSet<>()).add(docId);
        }
    }

    // Analyze a new document for plagiarism
    public Map<String, Double> analyzeDocument(String docId, String content) {
        Map<String, Integer> matchCounts = new HashMap<>();
        String[] words = content.split("\\s+");
        int totalNgrams = 0;

        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }
            String ngram = sb.toString().trim();
            totalNgrams++;

            Set<String> matchedDocs = ngramIndex.getOrDefault(ngram, Collections.emptySet());
            for (String matchedDoc : matchedDocs) {
                if (!matchedDoc.equals(docId)) {
                    matchCounts.put(matchedDoc, matchCounts.getOrDefault(matchedDoc, 0) + 1);
                }
            }
        }

        Map<String, Double> similarityPercentage = new HashMap<>();
        for (Map.Entry<String, Integer> entry : matchCounts.entrySet()) {
            similarityPercentage.put(entry.getKey(), (entry.getValue() * 100.0) / totalNgrams);
        }

        return similarityPercentage;
    }

    // Example usage
    public static void main(String[] args) {
        PlagiarismDetector detector = new PlagiarismDetector();

        // Simulate previous documents
        detector.indexDocument("essay_001", "This is a sample essay written by a student about machine learning.");
        detector.indexDocument("essay_002", "Machine learning is a fascinating field that has many applications in technology.");
        detector.indexDocument("essay_003", "This essay discusses different aspects of deep learning and neural networks.");

        // New document to check
        String newEssay = "This essay discusses machine learning and its applications in technology.";

        Map<String, Double> results = detector.analyzeDocument("essay_004", newEssay);

        for (Map.Entry<String, Double> entry : results.entrySet()) {
            System.out.println("Matched with " + entry.getKey() + " → Similarity: " + String.format("%.2f", entry.getValue()) + "%");
        }
    }
}
import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfWord = false;
}

class AutocompleteSystem {
    private TrieNode root = new TrieNode();
    private Map<String, Integer> frequencyMap = new HashMap<>();

    // Insert query into trie and frequency map
    public void addQuery(String query) {
        TrieNode node = root;
        for (char ch : query.toCharArray()) {
            node = node.children.computeIfAbsent(ch, c -> new TrieNode());
        }
        node.isEndOfWord = true;
        frequencyMap.put(query, frequencyMap.getOrDefault(query, 0) + 1);
    }

    // Return top K suggestions for prefix
    public List<String> getSuggestions(String prefix, int k) {
        TrieNode node = root;
        for (char ch : prefix.toCharArray()) {
            node = node.children.get(ch);
            if (node == null) return Collections.emptyList();
        }

        List<String> results = new ArrayList<>();
        collectAllWords(node, new StringBuilder(prefix), results);

        // Sort by frequency and take top k
        results.sort((a, b) -> frequencyMap.get(b) - frequencyMap.get(a));
        return results.size() > k ? results.subList(0, k) : results;
    }

    private void collectAllWords(TrieNode node, StringBuilder prefix, List<String> results) {
        if (node.isEndOfWord) results.add(prefix.toString());
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            prefix.append(entry.getKey());
            collectAllWords(entry.getValue(), prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public void printFrequencies() {
        System.out.println("Frequencies: " + frequencyMap);
    }

    public static void main(String[] args) {
        AutocompleteSystem auto = new AutocompleteSystem();

        // Adding queries
        auto.addQuery("java tutorial");
        auto.addQuery("java tutorial");
        auto.addQuery("javascript");
        auto.addQuery("java download");
        auto.addQuery("java 21 features");

        System.out.println("Suggestions for 'jav': " + auto.getSuggestions("jav", 3));
        auto.printFrequencies();
    }
}
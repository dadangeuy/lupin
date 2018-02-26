package lupin.decipher;

import lupin.cipher.Cipherable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseDecipher implements Decipherable {
    protected Cipherable cipher;
    protected String word = "the";
    protected int maxKeyLength = 10;
    protected int maxResult = 10;
    protected double matchThreshold = 0.0;

    @Override
    public void setCipher(Cipherable cipher) {
        this.cipher = cipher;
    }

    @Override
    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public void setLimit(int maxKeyLength, int maxResult, double matchThreshold) {
        this.maxKeyLength = maxKeyLength;
        this.maxResult = maxResult;
        this.matchThreshold = matchThreshold;
    }

    protected List<Map.Entry<String, Integer>> getWordFrequencies(String text) {
        int substringLength = word.length();
        Map<String, Integer> counter = new HashMap<>();
        for (int i = 0; i < text.length() - substringLength; i++) {
            String currentText = text.substring(i, i + substringLength);
            counter.put(currentText, counter.getOrDefault(currentText, 0) + 1);
        }
        return transformMapToSortedValueList(counter);
    }

    private List<Map.Entry<String, Integer>> transformMapToSortedValueList(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> result = new ArrayList<>(map.entrySet());
        result.sort((a, b) -> b.getValue() - a.getValue());
        return result;
    }

    protected int gcd(List<Integer> n) {
        int result = n.get(0);
        for (int i = 1; i < n.size() - 1; i++) result = gcd(result, n.get(i));
        return result;
    }

    protected List<Integer> getWordDistances(String text, String word) {
        List<Integer> positions = getWordPositions(text, word);
        List<Integer> distances = new ArrayList<>();
        for (int i = 0; i < positions.size() - 1; i++) distances.add(positions.get(i + 1) - positions.get(i));
        return distances;
    }

    private List<Integer> getWordPositions(String text, String word) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < text.length() - word.length(); i++) {
            String currentText = text.substring(i, i + word.length());
            if (currentText.equals(word)) positions.add(i);
        }
        return positions;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private int gcd(int x, int y) {
        while (y != 0) {
            int tempX = x;
            x = y;
            y = tempX % y;
        }
        return x;
    }
}

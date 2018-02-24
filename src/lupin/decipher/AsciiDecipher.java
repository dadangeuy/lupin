package lupin.decipher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsciiDecipher {
    private String text;

    public List<String> guessCipherKey(String text, String decipherKey, int maxKeyLength, int maxResult) {
        this.text = text;
        List<Map.Entry<String, Integer>> fsubstr = findSubstringFrequencies(decipherKey.length());
        List<String> possibleKey = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : fsubstr) {
            if (entry.getValue() <= 1 || possibleKey.size() > maxResult) break;
            try {
                String key = getTransformAlphanumericKey(entry.getKey(), decipherKey);
                if (key.length() <= maxKeyLength) possibleKey.add(key);
            } catch (Exception ignored) {
            }
        }
        return possibleKey;
    }

    private String getTransformAlphanumericKey(String from, String to) throws Exception {
        List<Integer> distance = getDistanceBetweenGuessText(from);
        int keyLength = distance.get(0);
        for (int i = 1; i < distance.size() - 1; i++) keyLength = gcd(keyLength, distance.get(i));
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < keyLength; i++) key.append('*');
        int firstOccur = text.indexOf(from) % keyLength;
        for (int i = 0; i < from.length(); i++) {
            int idx = (firstOccur + i) % keyLength;
            char newChar = from.charAt(i);
            newChar -= to.charAt(i);
            if (!isAlphanumeric(newChar)) throw new Exception("Invalid key");
            key.setCharAt(idx, newChar);
        }
        return key.toString();
    }

    private boolean isAlphanumeric(char c) {
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'));
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

    private List<Map.Entry<String, Integer>> findSubstringFrequencies(int length) {
        Map<String, Integer> counter = new HashMap<>();
        for (int i = 0; i < text.length() - length; i++) {
            String currentText = text.substring(i, i + length);
            counter.put(currentText, counter.getOrDefault(currentText, 0) + 1);
        }
        List<Map.Entry<String, Integer>> results = new ArrayList<>(counter.entrySet());
        results.sort((Map.Entry<String, Integer> stringIntegerEntry, Map.Entry<String, Integer> t1) ->
                             t1.getValue() - stringIntegerEntry.getValue());
        return results;
    }

    private List<Integer> getDistanceBetweenGuessText(String guess) {
        List<Integer> pos = new ArrayList<>();
        for (int i = 0; i < text.length() - guess.length(); i++) {
            String currentText = text.substring(i, i + guess.length());
            if (currentText.equals(guess)) pos.add(i);
        }
        List<Integer> dist = new ArrayList<>();
        for (int i = 0; i < pos.size() - 1; i++) {
            dist.add(pos.get(i + 1) - pos.get(i));
        }
        return dist;
    }
}

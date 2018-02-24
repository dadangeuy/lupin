package lupin.decipher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsciiDecipher {

    public String guessKey(String text) {
        List<Map.Entry<String, Integer>> fsubstr = findSubstringFrequencies(text, 3);
        String the = fsubstr.get(0).getKey();
        List<Integer> distance = getDistanceBetweenGuessText(text, the);
        int gcd = distance.get(1);
        for (int i = 1; i < distance.size() - 1; i++) {
            gcd = gcd(gcd, distance.get(i));
        }
        System.out.println(distance.toString());
        System.out.println(gcd);
        return null;
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

    private List<Map.Entry<String, Integer>> findSubstringFrequencies(String text, int length) {
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

    private List<Integer> getDistanceBetweenGuessText(String text, String guess) {
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

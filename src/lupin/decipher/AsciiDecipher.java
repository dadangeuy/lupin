package lupin.decipher;

import lupin.cipher.AsciiCipher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AsciiDecipher extends BaseDecipher {

    public AsciiDecipher() {
        setCipher(new AsciiCipher());
    }

    @Override
    public List<String> decipher(String text) {
        List<Map.Entry<String, Integer>> frequencies = getWordFrequencies(text);
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : frequencies) {
            String string = entry.getKey();
            int frequency = entry.getValue();

            try {
                if (frequency <= 1 || result.size() > maxResult) break;
                String key = getKeyFromStringTransformation(text, string);
                if (key.length() <= maxKeyLength && getMatchProbability(text, key) >= matchThreshold) result.add(key);
            } catch (Exception ignored) {}
        }
        return result;
    }

    private String getKeyFromStringTransformation(String text, String encrWord) throws Exception {
        List<Integer> distance = getWordDistances(text, encrWord);
        int keyLength = gcd(distance);
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < keyLength; i++) key.append('*');
        int firstOccur = text.indexOf(encrWord) % keyLength;
        for (int i = 0; i < encrWord.length(); i++) {
            int idx = (firstOccur + i) % keyLength;
            char keyChar = reverseKey(word.charAt(i), encrWord.charAt(i));
            if (!isAlphanumeric(keyChar)) throw new Exception("Invalid key");
            key.setCharAt(idx, keyChar);
        }
        return key.toString();
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

    private char reverseKey(char origin, char encrypted) {
        return (char) (encrypted - origin);
    }

    private double getMatchProbability(String text, String key) {
        if (matchThreshold == 0.0) return 1.0;
        cipher.setKey(key);
        int total = 0, match = 0;
        for (int i = 0; i < text.length(); i++) {
            if (key.charAt(i % key.length()) != '*') {
                total++;
                if (isKeyboardCharacter(cipher.decrypt(text.charAt(i), i))) match++;
            }
        }
        return 1.0 * match / total;
    }

    private boolean isKeyboardCharacter(char c) {
        return ((c >= ' ' && c <= '~') || c == '\n');
    }

    private boolean isAlphanumeric(char c) {
        return (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || ('0' <= c && c <= '9'));
    }
}

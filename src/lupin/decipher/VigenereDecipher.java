package lupin.decipher;

import lupin.cipher.VigenereCipher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VigenereDecipher extends BaseDecipher {

    public VigenereDecipher() {
        setCipher(new VigenereCipher());
    }

    @Override
    public List<String> decipher(String text) {
        List<Map.Entry<String, Integer>> frequencies = getWordFrequencies(text);
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : frequencies) {
            String string = entry.getKey();
            Integer frequency = entry.getValue();

            if (frequency <= 1 || result.size() >= maxResult) break;
            try {
                String key = getKeyFromStringTransformation(text, string);
                if (key.length() <= maxKeyLength) result.add(key);
            } catch (Exception ignored) { }
        }
        return result;
    }

    private String getKeyFromStringTransformation(String text, String encrWord) throws Exception {
        if (!isContainAlphabet(encrWord)) throw new Exception("Impossible to get key");
        List<Integer> distance = getWordDistances(text, encrWord);
        int keyLength = gcd(distance);
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < keyLength; i++) key.append('*');
        int firstOccur = text.indexOf(encrWord) % keyLength;
        for (int i = 0; i < encrWord.length(); i++) {
            int idx = (firstOccur + i) % keyLength;
            if (!isCaseMatch(word.charAt(i), encrWord.charAt(i))) throw new Exception("Invalid word");
            char keyChar = reverseKey(word.charAt(i), encrWord.charAt(i));
            if (keyChar != '*') key.setCharAt(idx, keyChar);
        }
        return key.toString();
    }

    private boolean isContainAlphabet(String string) {
        for (char c : string.toCharArray()) {
            if (isUppercase(c) || isLowercase(c))
                return true;
        }
        return false;
    }

    private boolean isCaseMatch(char a, char b) {
        return (a == b || (isUppercase(a) && isUppercase(b)) || (isLowercase(a) && isLowercase(b)));
    }

    private char reverseKey(char origin, char encrypted) {
        if (isUppercase(origin) && isUppercase(encrypted)) {
            int result = baseValue('A', encrypted) - baseValue('A', origin);
            result = positiveMod(result, 26);
            return (char) ('A' + result);
        } else if (isLowercase(origin) && isLowercase(encrypted)) {
            int result = baseValue('a', encrypted) - baseValue('a', origin);
            result = positiveMod(result, 26);
            return (char) ('a' + result);
        }
        return '*';
    }

    private int baseValue(char c, char baseChar) {
        return baseChar - c;
    }

    private boolean isAlphabet(char c) {
        return (isUppercase(c) || isLowercase(c));
    }

    private boolean isUppercase(char c) {
        return ('A' <= c && c <= 'Z');
    }

    private boolean isLowercase(char c) {
        return ('a' <= c && c <= 'z');
    }

    private int positiveMod(int value, int mod) {
        return (value % mod + mod) % mod;
    }

}

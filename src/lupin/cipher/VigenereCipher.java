package lupin.cipher;

public class VigenereCipher extends BaseCipher {

    @Override
    public char encrypt(char textChar, int position) {
        if (isUppercase(textChar)) {
            int result = baseValue(textChar, 'A') + baseValue(Character.toUpperCase(getKey(position)), 'A');
            result = positiveMod(result, 26);
            return (char) ('A' + result);
        } else if (isLowercase(textChar)) {
            int result = baseValue(textChar, 'a') + baseValue(Character.toLowerCase(getKey(position)), 'a');
            result = positiveMod(result, 26);
            return (char) ('a' + result);
        }
        return textChar;
    }

    @Override
    public char decrypt(char textChar, int position) {
        if (isUppercase(textChar)) {
            int result = baseValue(textChar, 'A') - baseValue(Character.toUpperCase(getKey(position)), 'A');
            result = positiveMod(result, 26);
            return (char) ('A' + result);
        } else if (isLowercase(textChar)) {
            int result = baseValue(textChar, 'a') - baseValue(Character.toUpperCase(getKey(position)), 'a');
            result = positiveMod(result, 26);
            return (char) ('a' + result);
        }
        return textChar;
    }

    private int baseValue(char c, char baseChar) {
        return c - baseChar;
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

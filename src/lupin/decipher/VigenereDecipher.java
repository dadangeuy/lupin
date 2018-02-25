package lupin.decipher;

import lupin.cipher.VigenereCipher;

public class VigenereDecipher extends BaseDecipher {

    public VigenereDecipher() {
        setCipher(new VigenereCipher());
    }

    @Override
    public char reverseEncrypt(char origin, char encrypted) throws Exception {
        if (isUppercase(origin) && isUppercase(encrypted)) {
            int value = baseValue('A', encrypted) - baseValue('A', origin);
            value = positiveMod(value, 26);
            return (char) ('A' + value);
        } else if (isLowercase(origin) && isLowercase(encrypted)) {
            int value = baseValue('a', encrypted) - baseValue('a', origin);
            value = positiveMod(value, 26);
            return (char) ('a' + value);
        }
        System.out.format("%c %c\n", origin, encrypted);
        throw new Exception("Impossible key");
    }

    private int baseValue(char c, char baseChar) {
        return baseChar - c;
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

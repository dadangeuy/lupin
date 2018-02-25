package lupin.cipher;

public class AsciiCipher extends BaseCipher {

    @Override
    public char encrypt(char textChar, int position) {
        return (char) (textChar + getKey(position));
    }

    @Override
    public char decrypt(char textChar, int position) {
        return (char) (textChar - getKey(position));
    }
}

package lupin.decipher;

import lupin.cipher.AsciiCipher;

public class AsciiDecipher extends BaseDecipher {

    public AsciiDecipher() {
        setCipher(new AsciiCipher());
    }

    @Override
    public char reverseEncrypt(char origin, char encrypted) {
        return (char) (encrypted - origin);
    }
}

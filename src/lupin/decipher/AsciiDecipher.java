package lupin.decipher;

import lupin.cipher.AsciiCipher;

public class AsciiDecipher extends BaseDecipher {

    public AsciiDecipher() {
        setCipher(new AsciiCipher());
    }
}

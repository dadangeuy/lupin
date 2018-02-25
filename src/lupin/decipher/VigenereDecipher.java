package lupin.decipher;

import lupin.cipher.VigenereCipher;

public class VigenereDecipher extends BaseDecipher {

    public VigenereDecipher() {
        setCipher(new VigenereCipher());
    }
}

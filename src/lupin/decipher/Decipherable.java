package lupin.decipher;

import lupin.cipher.Cipherable;

import java.util.List;

public interface Decipherable {
    void setCipher(Cipherable cipher);

    void setWord(String word);

    void setLimit(int maxKeyLength, int maxResult, double matchThreshold);

    List<String> decipher(String text);

    char reverseEncrypt(char origin, char encrypted) throws Exception;
}

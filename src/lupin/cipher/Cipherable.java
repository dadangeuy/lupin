package lupin.cipher;

public interface Cipherable {
    String getKey();

    void setKey(String key);

    char getKey(int position);

    String encrypt(String text);

    char encrypt(char textChar, int position);

    String decrypt(String text);

    char decrypt(char textChar, int position);
}

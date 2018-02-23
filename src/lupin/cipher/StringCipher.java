package lupin.cipher;

public interface StringCipher {
    String encrypt(String text, String key);

    String decrypt(String text, String key);
}

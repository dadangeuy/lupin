package lupin.cipher;

public class AsciiCipher {

    public String encrypt(String text, String key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char newChar = text.charAt(i);
            newChar += key.charAt(i % key.length());
            sb.append(newChar);
        }
        return sb.toString();
    }

    public String decrypt(String text, String key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char newChar = text.charAt(i);
            newChar -= key.charAt(i % key.length());
            sb.append(newChar);
        }
        return sb.toString();
    }
}

package lupin.cipher;

public abstract class BaseCipher implements Cipherable {
    private String key;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public char getKey(int position) {
        return key.charAt(position % key.length());
    }

    @Override
    public String encrypt(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            sb.append(encrypt(text.charAt(i), i));
        }
        return sb.toString();
    }

    @Override
    public String decrypt(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            sb.append(decrypt(text.charAt(i), i));
        }
        return sb.toString();
    }
}

package lupin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lupin.cipher.AsciiCipher;
import lupin.cipher.Cipherable;
import lupin.cipher.VigenereCipher;
import lupin.decipher.AsciiDecipher;
import lupin.decipher.Decipherable;
import lupin.decipher.VigenereDecipher;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    Program ini dibuat oleh Frieda dan Irsyad
    Aplikasi ini bisa meng-enkripsi/dekripsi file dengan memodifikasi byte data dari file sesuai dengan kunci ciphernya.
    Cara memodifikasi byte datanya mirip dengan vigenere cipher, yaitu dengan menambahkan(enkripsi)/mengurangi(dekripsi)
    byte dengan kata kuncinya. Yang membedakan, vigenere cipher menggunakan modulus untuk menghitung hasil enkripsi /
    dekripsinya, sementara aplikasi ini memanfaatkan 'overflow' agar tidak perlu menghitung modulusnya.
*/

public class AppController {
    private final FileChooser chooser = new FileChooser();
    @FXML
    private TextField chooseFileInfo;
    @FXML
    private TextField cipherKeyInput;
    @FXML
    private TextField decipherKeyInput;
    @FXML
    private TextArea fileDataPreview;
    @FXML
    private TextArea cipherFileDataPreview;
    @FXML
    private TextArea possibleKeyInput;
    @FXML
    private TextField keyLimitInput;
    @FXML
    private TextField resultLimitInput;
    @FXML
    private Slider matchThresholdInput;
    @FXML
    private ComboBox<String> cipherAlgorithmMenu;
    @FXML
    private ComboBox<String> decipherAlgorithmMenu;
    private String fileData;
    private String cipherFileData;
    private Cipherable cipher;
    private Decipherable decipher;

    public AppController() {
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt"));
    }

    @FXML
    public void initialize() {
        cipherAlgorithmMenu.getItems().addAll("ASCII Cipher", "Vigenere Cipher");
        decipherAlgorithmMenu.getItems().addAll("ASCII Decipher", "Vigenere Decipher");
        cipherAlgorithmMenu.getSelectionModel().selectFirst();
        onSelectCipherAlgorithm();
        decipherAlgorithmMenu.getSelectionModel().selectFirst();
        onSelectDecipherAlgorithm();
    }

    @FXML
    private void onChooseFile(ActionEvent event) {
        try {
            File file = chooser.showOpenDialog(getWindowFromEvent(event));
            this.fileData = FileUtils.readFileToString(file, Charset.defaultCharset());
            resetUI();
            chooseFileInfo.setText(file.getAbsolutePath());
            fileDataPreview.setText(fileData);
        } catch (NullPointerException ignored) {
        } catch (Exception e) {
            showException(e);
        }
    }

    @FXML
    private void onSelectCipherAlgorithm() {
        switch (cipherAlgorithmMenu.getValue()) {
            case "ASCII Cipher":
                cipher = new AsciiCipher();
                break;
            case "Vigenere Cipher":
                cipher = new VigenereCipher();
                break;
        }
    }

    @FXML
    private void onSelectDecipherAlgorithm() {
        switch (decipherAlgorithmMenu.getValue()) {
            case "ASCII Decipher":
                decipher = new AsciiDecipher();
                break;
            case "Vigenere Decipher":
                decipher = new VigenereDecipher();
                break;
        }
    }

    @FXML
    private void onEncryptFile() {
        try {
            cipher.setKey(getCipherKey());
            cipherFileData = cipher.encrypt(fileData);
            cipherFileDataPreview.setText(cipherFileData);
        } catch (Exception e) {
            showException(e);
        }
    }

    @FXML
    private void onDecryptFile() {
        try {
            cipher.setKey(getCipherKey());
            cipherFileData = cipher.decrypt(fileData);
            cipherFileDataPreview.setText(cipherFileData);
        } catch (Exception e) {
            showException(e);
        }
    }

    @FXML
    private void onSaveFile(ActionEvent event) {
        try {
            File file = chooser.showSaveDialog(getWindowFromEvent(event));
            if (!file.getName().endsWith(".txt")) file = new File(file.getParent(), file.getName() + ".txt");
            FileUtils.writeStringToFile(file, cipherFileData, Charset.defaultCharset());
        } catch (Exception e) {
            showException(e);
        }
    }

    @FXML
    private void onDecipherFile() {
        decipher.setLimit(getKeyLimit(), getResultLimit(), getMatchThreshold());
        decipher.setWord(getDecipherKey());
        List<String> possibleKey = decipher.decipher(fileData);
        possibleKeyInput.setText(formatResult(possibleKey));
    }

    private String getCipherKey() {
        return cipherKeyInput.getText();
    }

    private String getDecipherKey() {
        return decipherKeyInput.getText();
    }

    private int getKeyLimit() {
        try {
            return Integer.valueOf(keyLimitInput.getText());
        } catch (Exception e) {
            return 10;
        }
    }

    private int getResultLimit() {
        try {
            return Integer.valueOf(resultLimitInput.getText());
        } catch (Exception e) {
            return 10;
        }
    }

    private double getMatchThreshold() {
        return matchThresholdInput.getValue();
    }

    private Window getWindowFromEvent(ActionEvent event) {
        return ((Node) event.getTarget()).getScene().getWindow();
    }

    private void showException(Exception e) {
        e.printStackTrace();
    }

    private void resetUI() {
        fileDataPreview.setText("");
        cipherFileDataPreview.setText("");
        possibleKeyInput.setText("");
    }

    private String formatResult(List<String> list) {
        if (list.isEmpty()) return "Empty Result";
        Map<String, Integer> counter = new HashMap<>();
        for (String str : list) counter.put(str, counter.getOrDefault(str, 0) + 1);
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(counter.entrySet());
        sortedList.sort((a, b) -> b.getValue() - a.getValue());
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : sortedList)
            sb.append(entry.getKey()).append(", ");
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }
}

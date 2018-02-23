package lupin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

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
    private TextArea filePreview;
    private String key;
    private File file;
    private byte[] fileData;
    private File newFile;
    private byte[] newFileData;

    public AppController() {
        chooser.setTitle("Choose a file to encrypt/decrypt");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt"));
    }

    @FXML
    private void onChooseFile(ActionEvent event) {
        File file = chooser.showOpenDialog(getWindowFromEvent(event));
        if (file != null) setFile(file);
    }

    @FXML
    private void onEncryptFile() {
        try {
            fetchChiperKey();
            encryptFileData();
            setNewFileWithPrefix("encr-");
            saveNewFile();
        } catch (Exception e) {
            showException(e);
        }
    }

    @FXML
    private void onDecryptFile() {
        try {
            fetchChiperKey();
            decryptFileData();
            setNewFileWithPrefix("decr-");
            saveNewFile();
        } catch (Exception e) {
            showException(e);
        }
    }

    private void fetchChiperKey() throws Exception {
        key = cipherKeyInput.getText();
        if (key == null || key.isEmpty()) throw new Exception("Key can't be empty.");
    }

    private void setNewFileWithPrefix(String prefix) {
        setNewFile(new File(file.getParent(), prefix + file.getName()));
    }

    private void setFile(File file) {
        try {
            this.file = file;
            chooseFileInfo.setText(this.file.getAbsolutePath());
            fetchFileData();
        } catch (Exception e) {
            showException(e);
        }
    }

    private void setNewFile(File newFile) {
        this.newFile = newFile;
    }

    private void fetchFileData() throws IOException {
        fileData = FileUtils.readFileToByteArray(file);
        previewFile();
    }

    private void previewFile() {
        filePreview.setText(new String(fileData));
    }

    private void encryptFileData() {
        copyFileDataToNewFileData();
        for (int i = 0; i < newFileData.length; i++) {
            newFileData[i] += key.charAt(i % key.length());
        }
    }

    private void decryptFileData() {
        copyFileDataToNewFileData();
        for (int i = 0; i < newFileData.length; i++) {
            newFileData[i] -= key.charAt(i % key.length());
        }
    }

    private void copyFileDataToNewFileData() {
        newFileData = new byte[fileData.length];
        System.arraycopy(fileData, 0, newFileData, 0, fileData.length);
    }

    private void saveNewFile() throws IOException {
        FileUtils.writeByteArrayToFile(newFile, newFileData);
    }

    private Window getWindowFromEvent(ActionEvent event) {
        return ((Node) event.getTarget()).getScene().getWindow();
    }

    private void showException(Exception e) {
        e.printStackTrace();
    }
}

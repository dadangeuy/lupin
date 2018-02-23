package lupin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.*;
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
    @FXML
    private TextField chooseFileInfo;
    @FXML
    private TextField cipherKeyInput;
    private File file;
    private final FileChooser chooser = new FileChooser();
    private byte[] fileData;
    private String key;

    public AppController() {
        chooser.setTitle("Choose a file to encrypt/decrypt");
    }

    @FXML
    private void onChooseFile(ActionEvent event) {
        setFile(chooser.showOpenDialog(getWindowFromEvent(event)));
    }

    @FXML
    private void onEncryptFile(ActionEvent event) {
        try {
            fetchChiperKey();
            fetchFileData();
            encryptFileData();
            setNewFileWithPrefix("encr-");
            saveFileData();
        } catch (Exception e) {
            showException(e);
        }
    }

    @FXML
    private void onDecryptFile(ActionEvent event) {
        try {
            fetchChiperKey();
            fetchFileData();
            decryptFileData();
            setNewFileWithPrefix("decr-");
            saveFileData();
        } catch (Exception e) {
            showException(e);
        }
    }

    private void fetchChiperKey() throws Exception {
        key = cipherKeyInput.getText();
        if (key == null || key.isEmpty()) throw new Exception("Key can't be empty.");
    }

    private void fetchFileData() throws IOException {
        fileData = FileUtils.readFileToByteArray(file);
    }

    private void setNewFileWithPrefix(String prefix) {
        setFile(new File(file.getParent(), prefix + file.getName()));
    }

    private void setFile(File newFile) {
        this.file = newFile;
        if (file != null) chooseFileInfo.setText(this.file.getAbsolutePath());
    }

    private void encryptFileData() {
        for (int i = 0; i < fileData.length; i++) {
            fileData[i] += key.charAt(i % key.length());
        }
    }

    private void decryptFileData() {
        for (int i = 0; i < fileData.length; i++) {
            fileData[i] -= key.charAt(i % key.length());
        }
    }

    private void saveFileData() throws IOException {
        FileUtils.writeByteArrayToFile(file, fileData);
    }

    private Window getWindowFromEvent(ActionEvent event) {
        return ((Node) event.getTarget()).getScene().getWindow();
    }

    private void showException(Exception e) {
        e.printStackTrace();
    }
}

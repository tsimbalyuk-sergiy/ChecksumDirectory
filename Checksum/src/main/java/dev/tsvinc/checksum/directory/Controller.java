package dev.tsvinc.checksum.directory;

import dev.tsvinc.checksum.directory.hash.HashCRC32;
import dev.tsvinc.checksum.directory.hash.HashMD5;
import dev.tsvinc.checksum.directory.hash.HashSHA1;
import dev.tsvinc.checksum.directory.hash.HashSHA256;
import dev.tsvinc.checksum.directory.hash.HashSHA512;
import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Controller implements Initializable, Observer {
  private static final Logger logger = LogManager.getLogger(Gui.class.getName());
  private static final String PREF_FILE_INPUT = "fninput";
  private Worker worker = new Worker();
  private Preferences preferences = Preferences.userNodeForPackage(Controller.class);
  private File fPathInput = null;

  @FXML private BorderPane rootPane;

  @FXML private Button btnInput;
  @FXML private Button btnRun;
  @FXML private TextField txtFileName;
  @FXML private TextField txtSize;
  @FXML private TextField txtCrc32;
  @FXML private TextField txtMd5;
  @FXML private TextField txtSha1;
  @FXML private TextField txtSha256;
  @FXML private TextField txtSha512;

  @Override
  public void update(Observable o, Object arg) {
    final String sTmpFilesize;
    final String sTmpCRC32;
    final String sTmpMD5;
    final String sTmpSHA1;
    final String sTmpSHA256;
    final String sTmpSHA512;

    if (arg instanceof Long) {
      Long l = (Long) arg;
      sTmpFilesize = l.toString();
      Platform.runLater(() -> txtSize.setText(sTmpFilesize));
    }

    if (arg instanceof HashCRC32) {
      HashCRC32 hash = (HashCRC32) arg;
      sTmpCRC32 = hash.getHash();
      Platform.runLater(() -> txtCrc32.setText(sTmpCRC32));
    }

    if (arg instanceof HashMD5) {
      HashMD5 hash = (HashMD5) arg;
      sTmpMD5 = hash.getHash();
      Platform.runLater(() -> txtMd5.setText(sTmpMD5));
    }

    if (arg instanceof HashSHA1) {
      HashSHA1 hash = (HashSHA1) arg;
      sTmpSHA1 = hash.getHash();
      Platform.runLater(() -> txtSha1.setText(sTmpSHA1));
    }
    if (arg instanceof HashSHA256) {
      HashSHA256 hash = (HashSHA256) arg;
      sTmpSHA256 = hash.getHash();
      Platform.runLater(() -> txtSha256.setText(sTmpSHA256));
    }
    if (arg instanceof HashSHA512) {
      HashSHA512 hash = (HashSHA512) arg;
      sTmpSHA512 = hash.getHash();
      Platform.runLater(
          () -> {
            txtSha512.setText(sTmpSHA512);
            btnRun.setDisable(false);
            rootPane.getScene().setCursor(Cursor.DEFAULT);
          });
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    logger.trace("Entering application.");
    logger.traceEntry();
    btnInput.setOnAction(event -> getInputFile());
    btnRun.setOnAction(event -> doRun());
    btnRun.setDisable(true);

    txtSize.setOnMouseClicked(event -> mouseClick(event, txtSize));
    txtCrc32.setOnMouseClicked(event -> mouseClick(event, txtCrc32));
    txtMd5.setOnMouseClicked(event -> mouseClick(event, txtMd5));
    txtSha1.setOnMouseClicked(event -> mouseClick(event, txtSha1));
    txtSha256.setOnMouseClicked(event -> mouseClick(event, txtSha256));
    txtSha512.setOnMouseClicked(event -> mouseClick(event, txtSha512));

    String sTmp = preferences.get(PREF_FILE_INPUT, null);
    if (null != sTmp) {
      fPathInput = new File(sTmp);
      fPathInput = new File(fPathInput.getPath());
      worker.setInputFilename(sTmp);
    }
  }

  private void mouseClick(MouseEvent e, TextField tf) {
    tf.selectAll();
    if (2 == e.getClickCount()) {
      String sTmp = tf.getText();
      final Clipboard clipboard = Clipboard.getSystemClipboard();
      final ClipboardContent content = new ClipboardContent();
      content.putString(sTmp);
      clipboard.setContent(content);
    } // if
  }

  private void doRun() {
    rootPane.getScene().setCursor(Cursor.WAIT);

    logger.info("clicked");
    btnRun.setDisable(true);
    txtSize.clear();
    txtCrc32.clear();
    txtMd5.clear();
    txtSha1.clear();
    txtSha256.clear();
    txtSha512.clear();

    worker.addObserver(this);
    Thread thread = new Thread(worker);
    thread.start();
  }

  void newInputFile(File f) {
    txtFileName.setText(f.getAbsolutePath());
    txtSize.clear();
    txtCrc32.clear();
    txtMd5.clear();
    txtSha1.clear();
    txtSha256.clear();
    txtSha512.clear();
    worker.setInputFilename(f.getAbsolutePath());
    preferences.put(PREF_FILE_INPUT, f.getParentFile().getAbsolutePath());
    btnRun.setDisable(false);
  }

  private void getInputFile() {
    FileChooser fileChooser = new FileChooser();
    if (fPathInput != null && fPathInput.canRead() && fPathInput.isDirectory()) {
      fileChooser.setInitialDirectory(fPathInput);
    }
    fileChooser.setTitle("Open Input File");
    Scene scene = rootPane.getScene();
    Window w = scene.getWindow();
    File f = fileChooser.showOpenDialog(w);
    if (f != null) {
      newInputFile(f);
    }
  }
}

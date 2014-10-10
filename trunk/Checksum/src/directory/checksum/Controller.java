package directory.checksum;

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
	private directory.checksum.Worker worker = new directory.checksum.Worker();
	private Preferences prefs = Preferences.userNodeForPackage(Controller.class);
	static final Logger logger = LogManager.getLogger(Gui.class.getName());

	final String PREF_FILE_INPUT = "fninput";

	private File fPathInput = null;

	@FXML private BorderPane myroot;

	@FXML private Button btninput;
	@FXML private Button btnrun;
	@FXML private TextField txtfilename;
	@FXML private TextField txtsize;
	@FXML private TextField txtcrc32;
	@FXML private TextField txtmd5;
	@FXML private TextField txtsha1;
	@FXML private TextField txtsha256;
	@FXML private TextField txtsha512;

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
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					txtsize.setText(sTmpFilesize);
				}
			});
		} // if Long

		if (arg instanceof HashCRC32) {
			HashCRC32 hash = (HashCRC32) arg;
			sTmpCRC32 = hash.getHash();
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					txtcrc32.setText(sTmpCRC32);
				}
			});
		} // if CRC32

		if (arg instanceof HashMD5) {
			HashMD5 hash = (HashMD5) arg;
			sTmpMD5 = hash.getHash();
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					txtmd5.setText(sTmpMD5);
				}
			});
		} // if MD5

		if (arg instanceof HashSHA1) {
			HashSHA1 hash = (HashSHA1) arg;
			sTmpSHA1 = hash.getHash();
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					txtsha1.setText(sTmpSHA1);
				}
			});
		} // if SHA1

		if (arg instanceof HashSHA256) {
			HashSHA256 hash = (HashSHA256) arg;
			sTmpSHA256 = hash.getHash();
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					txtsha256.setText(sTmpSHA256);
				}
			});
		} // if SHA256

		if (arg instanceof HashSHA512) {
			HashSHA512 hash = (HashSHA512) arg;
			sTmpSHA512 = hash.getHash();
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					txtsha512.setText(sTmpSHA512);
					btnrun.setDisable(false);
					myroot.getScene().setCursor(Cursor.DEFAULT);
				}
			});
		} // if SHA512

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		logger.trace("Entering application.");
		logger.entry();
		btninput.setOnAction(event -> getInputFile());
		btnrun.setOnAction(event -> doRun());
		btnrun.setDisable(true);

		txtsize.setOnMouseClicked(event -> mouseClick(event, txtsize));
		txtcrc32.setOnMouseClicked(event -> mouseClick(event, txtcrc32));
		txtmd5.setOnMouseClicked(event -> mouseClick(event, txtmd5));
		txtsha1.setOnMouseClicked(event -> mouseClick(event, txtsha1));
		txtsha256.setOnMouseClicked(event -> mouseClick(event, txtsha256));
		txtsha512.setOnMouseClicked(event -> mouseClick(event, txtsha512));
		
		String sTmp = prefs.get(PREF_FILE_INPUT, null);
		if(null != sTmp) {
			fPathInput = new File(sTmp);
			fPathInput = new File(fPathInput.getPath());
			worker.setInputFilename(sTmp);
		}

	}

	private void mouseClick(MouseEvent e, TextField tf) {
		tf.selectAll();
		if(2 == e.getClickCount()) {
			String sTmp = tf.getText();
			final Clipboard clipboard = Clipboard.getSystemClipboard();
	        final ClipboardContent content = new ClipboardContent();
	        content.putString(sTmp);
	        clipboard.setContent(content);
		} // if
	}
	
	private void doRun() {
		myroot.getScene().setCursor(Cursor.WAIT);

		logger.info("clicked");
		btnrun.setDisable(true);
		txtsize.clear();
		txtcrc32.clear();
		txtmd5.clear();
		txtsha1.clear();
		txtsha256.clear();
		txtsha512.clear();

		worker.addObserver(this);
		Thread thread = new Thread(worker);
        thread.start();
	}

	public void newInputFile(File f) {
		txtfilename.setText(f.getAbsolutePath());
		txtsize.clear();
		txtcrc32.clear();
		txtmd5.clear();
		txtsha1.clear();
		txtsha256.clear();
		txtsha512.clear();
		worker.setInputFilename(f.getAbsolutePath());
		prefs.put(PREF_FILE_INPUT, f.getParentFile().getAbsolutePath());
		btnrun.setDisable(false);
	}
	
	private void getInputFile() {
		FileChooser fileChooser = new FileChooser();
		if(null != fPathInput) {
			if(fPathInput.canRead() && fPathInput.isDirectory()) {
				fileChooser.setInitialDirectory(fPathInput);
			} // if
		} // if
		fileChooser.setTitle("Open Input File");
		Scene scene = myroot.getScene();
		Window w = scene.getWindow();
		File f = fileChooser.showOpenDialog(w);
		if(f != null) {
			newInputFile(f);
		}
	}
	
}

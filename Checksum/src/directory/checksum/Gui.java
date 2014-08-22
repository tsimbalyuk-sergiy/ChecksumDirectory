package directory.checksum;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Gui extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Checksum Directory");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("Logo512.png")));
		stage.setMinHeight(350);
		stage.setMinWidth(650);
		stage.show();
	}
}

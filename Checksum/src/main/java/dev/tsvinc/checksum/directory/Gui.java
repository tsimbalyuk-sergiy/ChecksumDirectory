package dev.tsvinc.checksum.directory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

public class Gui extends Application {
  // TODO: fix logging

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui.fxml"));
    Parent root = fxmlLoader.load();
    Controller controller = fxmlLoader.getController();

    Scene scene = new Scene(root);
    scene.setOnDragOver(
        event -> {
          Dragboard db = event.getDragboard();
          if (db.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
          } else {
            event.consume();
          }
        });
    scene.setOnDragDropped(
        event -> {
          Dragboard db = event.getDragboard();
          boolean success = false;
          if (db.hasFiles()) {
            success = true;
            db.getFiles().forEach(controller::newInputFile);
          }
          event.setDropCompleted(success);
          event.consume();
        });

    stage.setScene(scene);
    stage.setTitle("Checksum Directory");
    stage.getIcons().add(new Image(getClass().getResourceAsStream("/Logo512.png")));
    stage.setMinHeight(350);
    stage.setMinWidth(650);
    stage.show();
  }
}

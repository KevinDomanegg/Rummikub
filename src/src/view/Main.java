package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.GameController;

import java.awt.*;


public class Main extends Application {
  private StartController startController;
  //WaitController waitController;
  GameController gameController; //TODO: Hide

  public static void main(String[] args) {
    launch(args);
  }

  //TODO: Catch exception
  @Override
  public void start(Stage primaryStage) throws Exception {
    // FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml")); //TODO: Change
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Start.fxml"));
    Parent root = loader.load();
    //gameController = loader.getController();
    startController = loader.getController();

    primaryStage.setTitle("Rummikub");
    //Scene scene = resolution(root);
    Scene scene = new Scene(root, 1024, 768);
    //scene.getStylesheets().add("view/gameStyle.css"); //TODO: Hide
    primaryStage.setScene(scene);
    //primaryStage.setFullScreen(true);
    primaryStage.show();
  }

  /**
   * Creates scene depending on the user device's resolution
   *
   * @param root parent root
   * @return new scene with user's resolution
   */
  Scene resolution(Parent root) {
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    return new Scene(root, screen.width, screen.height);
  }
}

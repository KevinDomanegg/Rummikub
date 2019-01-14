package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("game.fxml"));
        primaryStage.setTitle("Rummikub");
        //Scene scene = resolution(root);
        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add("gameStyle.css");
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    /** Creates scene depending on the user device's resolution
     *
     * @param root parent root
     * @return new scene with user's resolution
     */
    Scene resolution(Parent root) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        return new Scene(root, screen.width, screen.height);
    }
}

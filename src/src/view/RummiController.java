package view;

import game.Game;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.client.RummiClient;

import java.awt.*;
import java.io.IOException;


public class RummiController extends Application {
    //StartController startController;
    private WaitController waitController;
    private GameController gameController;
    private StartController startController;
    private Parent root;
    private Stage window;
    private FXMLLoader loader;

    public static void main(String[] args) {
        launch(args);
    }

    //TODO: Catch exception
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Rummikub");
        loader = new FXMLLoader(getClass().getResource("Start.fxml"));
        root = loader.load();
        startController = loader.getController();
        window.setScene(new Scene(root, 1024, 768));
        window.show();
    }



  /*      primaryStage.setTitle("Rummikub");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Start.fxml"));
        Parent root = loader.load();
        startController = loader.getController();
        //Scene scene = resolution(root);
        Scene startScene = new Scene(root, 1024, 768);
        primaryStage.setScene(startScene);
        primaryStage.show();*/

/*        loader = new FXMLLoader(getClass().getResource("Game.fxml"));
        root = loader.load();
        gameController = loader.getController();
        Scene gameScene = new Scene(root, 1024, 768);
        gameScene.getStylesheets().add("gameStyle.css");

        loader = new FXMLLoader(getClass().getResource("Wait.fxml"));
        root = loader.load();
        waitController = loader.getController();
        Scene waitScene = new Scene(root, 1024, 768);*/
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

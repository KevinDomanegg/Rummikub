package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class WaitController {

    @FXML private Button startGameButton;

    @FXML
    void startGame() throws IOException {
        switchToGameView();
    }

    @FXML
    private void switchToGameView() throws IOException {
        Stage stage = (Stage) startGameButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
        Parent root = loader.load();
        Scene gameScene = new Scene(root, 1024, 768);
        gameScene.getStylesheets().add("gameStyle.css");
        stage.setScene(gameScene);
    }
}

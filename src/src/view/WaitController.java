package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class WaitController {

  private NetworkController networkController;
  private GameController gameController;

  @FXML
  private Button startGameButton;

  @FXML
  private void startGame() throws IOException {
    networkController.sendStartRequest();
    switchToGameView();
  }

  void setNetworkController(NetworkController controller) {
    this.networkController = controller;
  }

  @FXML
  void switchToGameView() throws IOException {
    Stage stage = (Stage) startGameButton.getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
    Parent root = loader.load();

    gameController = loader.getController();

    gameController.setNetworkController(networkController);
    networkController.setGameController(gameController);

    Scene gameScene = new Scene(root, 1024, 768);
    gameScene.getStylesheets().add("View/gameStyle.css");
    stage.setScene(gameScene);
  }


}

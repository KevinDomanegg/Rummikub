package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import network.client.RequestBuilder;

import java.io.IOException;

public class WaitController {

  private RequestBuilder requestBuilder;
  private String ipAddress;

  @FXML
  private Button startGameButton;

  @FXML
  private void startGame() {
    requestBuilder.sendStartRequest();
    switchToGameView();
  }

  void setRequestBuilder(RequestBuilder requestBuilder) {
    this.requestBuilder = requestBuilder;
  }

  void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  @FXML
  String getIpAddress() {
    return ipAddress;
  }

  public void switchToGameView() {
    Stage stage = (Stage) startGameButton.getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
    Parent root = null;
    try {
      root = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    GameController gameController = loader.getController();

    gameController.setRequestBuilder(requestBuilder);
//    networkController.setGameController(gameController);

    Scene gameScene = new Scene(root, 1024, 768);
    gameScene.getStylesheets().add("view/gameStyle.css");
    stage.setScene(gameScene);
  }
}

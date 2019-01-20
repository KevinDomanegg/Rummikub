package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.client.RequestBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WaitController implements Initializable {

  private RequestBuilder requestBuilder;
  private NetworkController networkController;

  @FXML
  private Text ipAddress;

  @FXML
  private Button startGameButton;

  @FXML
  private Text player0;

  @FXML
  private Text player1;

  @FXML
  private Text player2;

  @FXML
  private Text player3;

  @FXML
  private void startGame() {
    requestBuilder.sendStartRequest();
  }

  void setNetworkController(NetworkController networkController) {
    this.networkController = networkController;
  }

  void setRequestBuilder(RequestBuilder requestBuilder) {
    this.requestBuilder = requestBuilder;
  }

  void setIpAddress(String ipAddress) {
    this.ipAddress.setText(ipAddress);
  }

 void setPlayerUsername(String username, int id) {
    switch (id) {
      case 0:
        this.player0.setText(username);
        break;
      case 1:
        this.player1.setText(username);
        break;
      case 2:
        this.player2.setText(username);
        break;
      case 3:
        this.player3.setText(username);
    }
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
    networkController.setGameController(gameController);

    Scene gameScene = new Scene(root, 1024, 768);
    gameScene.getStylesheets().add("view/gameStyle.css");
    stage.setScene(gameScene);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //ipAddress.setText();
  }
}

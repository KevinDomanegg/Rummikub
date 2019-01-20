package view;

import java.util.List;
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
import network.client.RummiClient;

public class WaitController implements Initializable {

  private RequestBuilder requestBuilder;
  private NetworkController networkController;
  private ClientModel model;

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

// void setPlayerUsername(String username) {
//    names.add(username);
// }

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
    gameController.setModel(model);

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

  void setPlayerNames(List<String> names) {
    model.setPlayerNames(names);
    for (int i = 0; i < names.size(); i++) {
      switch (i) {
        case 0:
          player0.setText(names.get(0));
          break;
        case 1:
          player1.setText(names.get(1));
          break;
        case 2:
          player2.setText(names.get(2));
          break;
        case 3:
        default:
          player3.setText(names.get(3));
      }
    }
  }

  public void setModel(ClientModel model) {
    this.model = model;
  }
}

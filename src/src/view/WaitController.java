package view;

import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.client.RequestBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WaitController implements Initializable {

  private RequestBuilder requestBuilder;
  private NetworkController networkController;
  private ClientModel model;

  @FXML
  private Label waitingState;

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

  public void setModel(ClientModel model) {
    if (model.isHost()) {
      waitingState.setText("hosting game");
    }
    this.model = model;
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

  void switchToGameView() {
    Stage stage = (Stage) startGameButton.getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
    Parent root = loader.getRoot();
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
//    startGameButton.visibleProperty().bind(model.hostProperty());
    //ipAddress.setText();
  }

  void setPlayerNames(List<String> names) {
    model.setPlayerNames(names);
    int numberOfPlayers = names.size();
    if (numberOfPlayers > 1 && model.isHost()) {
      startGameButton.setVisible(true);
    } else {
      startGameButton.setVisible(false);
    }
    for (int i = 0; i < numberOfPlayers; i++) {
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
}

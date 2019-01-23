package view;

import communication.gameinfo.StoneInfo;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
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

  private Stage stage;

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

  Stage getStage() {
    return stage;
  }

  void setPlayerNames(List<String> names) {
    model.setPlayerNames(names);
    int size = names.size();
    switch (size) {
      case 4:
        player3.setText(names.get(3));
      case 3:
        player2.setText(names.get(2));
      case 2:
        player1.setText(names.get(1));
      case 1:
        player0.setText(names.get(0));
      default:
    }
    if (model.isHost()) {
      if (size > 1) {
        // start button visible
        startGameButton.setVisible(true);
        return;
      }
    }
    // start button not visible
    startGameButton.setVisible(false);
  }

 synchronized void switchToGameView() {
    networkController.stopMusicInWaiting();
    synchronized (networkController) {
      stage = (Stage) startGameButton.getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
      Parent root = loader.getRoot();
      //loader.setRoot(this);
      try {
        root = loader.load();
      } catch (IOException e) {
        e.printStackTrace();
      }

      GameController gameController = loader.getController();
      gameController.setModel(model);

      gameController.setRequestBuilder(requestBuilder);
      gameController.setNetworkController(networkController);
      networkController.setGameController(gameController);

      Scene gameScene = new Scene(root, 1024, 768);
      gameScene.getStylesheets().add("view/gameStyle.css");
      stage.setScene(gameScene);
      notifyAll();
      //System.out.println("switched to game");
       stage.setOnCloseRequest(e -> {
        System.out.println("klicked  on x");
        // Closes the Timer
        gameController.stopTimer();
        networkController.killThreads();
        Platform.exit();
      });
    }

  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //ipAddress.setText();
  }

  void setModel(ClientModel model) {
    this.model = model;
    if (model.isHost()) {
      waitingState.setText("hosting Game");
    }
    ipAddress.setText(model.getServerIP());
  }

  public void setTable(StoneInfo[][] table) {
    model.setTable(table);
  }

  public void setPlayerHand(StoneInfo[][] hand) {
    model.setHand(hand);
  }

  public void notifyTurn() {
    model.notifyTurn();
  }

  public void notifyCurrentPlayer(int playerID) {
    model.setCurrentPlayer(playerID);
  }

  public void setHandSizes(List<Integer> sizes) {
    model.setHandSizes(sizes);
  }

  public void setBagSize(int bagSize) {
    model.setBagSize(bagSize);
  }
}

package view;

import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.music.Music;
import java.net.URL;
import java.util.ResourceBundle;

public class WaitController implements Initializable {

//  private RequestBuilder requestBuilder;
//  private NetworkController networkController;
  private MainController mainController;

//  private ClientModel model;

  @FXML
  private Text waitingState;

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
  private Button notMuteButton;

  @FXML
  private Button muteButton;

  private Stage stage;

  @FXML
  private void startGame() {
    mainController.sendStartRequest();
  }

//  void setNetworkController(NetworkController networkController) {
//    this.networkController = networkController;
//  }

  void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

//  void setRequestBuilder(RequestBuilder requestBuilder) {
//    this.requestBuilder = requestBuilder;
//  }

  Stage getStage() {
    return stage;
  }

  void setPlayerNames(List<String> names) {
    System.out.println(names);
//    model.setPlayerNames(names);
    switch (names.size()) {
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
//    Platform.runLater(() -> {
//    });
//    if (model.isHost()) {
//      if (size > 1) {
//        // start button visible
//        startGameButton.setVisible(true);
//        return;
//      }
//    }
    // start button not visible
//    startGameButton.setVisible(false);
  }

// synchronized void switchToGameView() {
//    networkController.stopMusicInWaiting();
//    synchronized (networkController) {
//      stage = (Stage) startGameButton.getScene().getWindow();
//      FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
//      Parent root = loader.getRoot();
//      loader.setRoot(this);
//      try {
//        root = loader.load();
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//
//      GameController gameController = loader.getController();
//      gameController.setModel(model);
//
//      gameController.setRequestBuilder(requestBuilder);
//      gameController.setNetworkController(networkController);
//      networkController.setGameController(gameController);
//
//      Scene gameScene = new Scene(root, 1024, 768);
//      gameScene.getStylesheets().add("view/gameStyle.css");
//      stage.setScene(gameScene);
//      notifyAll();
//      System.out.println("switched to game");
//       stage.setOnCloseRequest(e -> {
//        System.out.println("klicked  on x");
//        // Closes the Timer
//        gameController.stopTimer();
//        networkController.killThreads();
//        Platform.exit();
//      });
//    }
//
//  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //ipAddress.setText();
  }

//  void setModel(ClientModel model) {
//    this.model = model;
//    if (model.isHost()) {
//      waitingState.setText("Hosting Game");
//    }
//    ipAddress.setText(model.getServerIP());
//  }

//  public void setTable(StoneInfo[][] table) {
//    model.setTable(table);
//  }

//  public void setPlayerHand(StoneInfo[][] hand) {
//    model.setHand(hand);
//  }

//  public void notifyTurn() {
//    model.notifyTurn();
//  }

//  public void notifyCurrentPlayer(int playerID) {
//    model.setCurrentPlayer(playerID);
//  }

//  public void setHandSizes(List<Integer> sizes) {
//    model.setHandSizes(sizes);
//  }

//  public void setBagSize(int bagSize) {
//    model.setBagSize(bagSize);
//  }

  @FXML
  private void mute() {
    Music.muteSoundOfWait();
    muteButton.setVisible(false);
    notMuteButton.setVisible(true);
  }

  @FXML
  private void unMute() {
    Music.playMusicNow();
    notMuteButton.setVisible(false);
    muteButton.setVisible(true);
  }

  @FXML private void quitWating() {
    mainController.quit();
  }

  void setServerIP(String serverIP) {
    ipAddress.setText(serverIP);
  }

  @FXML
  private void showHelpScene() {
    mainController.showHelpScene();
  }
}

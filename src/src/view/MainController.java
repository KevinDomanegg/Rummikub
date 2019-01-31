package view;

import communication.gameinfo.StoneInfo;
import communication.request.ConcreteMove;
import communication.request.RequestID;
import communication.request.SimpleRequest;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import network.client.GameInfoHandler;
import network.client.RequestBuilder;
import network.client.RummiClient;
import network.server.RummiServer;
import view.music.Music;

public class MainController implements Controller {

  private GameController gameController;
  private StartController startController;
  private WaitController waitController;
  private WinnerController winnerController;
  private ErrorController errorController;
  private Stage primaryStage;
  private RummiClient client;
  private RequestBuilder requestBuilder;
  private String serverIP;


  MainController(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  private void switchScene(String fxml) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
    Parent root = loader.load();
    switch (fxml) {
      case "start.fxml":
        Music.selectMusic("start");
        Music.playMusicNow();
        startController = loader.getController();
        startController.setMainController(this);
        break;
      case "wait.fxml":
        Music.selectMusic("wait");
        Music.playMusicNow();
        waitController = loader.getController();
        waitController.setMainController(this);
        break;
      case "game.fxml":
        Music.selectMusic("game");
        gameController = loader.getController();
        gameController.setMainController(this);
        break;
    }
    Scene scene = new Scene(root, 1500, 900);
    Platform.runLater(() -> {
      primaryStage.setMaxWidth(1500);
      primaryStage.setMaxHeight(900);
      primaryStage.setResizable(false);

      primaryStage.setScene(scene);
      primaryStage.show();
    });
  }

  void switchToWaitScene() throws IOException {
    switchScene("wait.fxml");
    waitController.setServerIP(serverIP);
  }

  void switchToStartScene() throws IOException {
    // delete winner- and gameController
    winnerController = null;
    gameController = null;
    switchScene("start.fxml");
  }

  private void switchToGameScene() throws IOException{
     switchScene("game.fxml");

  }

  void returnToStartView() {
    gameController.stopTimer();
    Stage primaryStage = waitController.getStage();
    startController.returnToStart(primaryStage);
  }

  public void noServerAvailable() {
    showErrorGotToStart("Host is not connected");
  }

  public void connectionRejected() {
    showErrorGotToStart("The Host has rejected the connection.\n" +
            "There might be no spot left in the game!");
  }

  private void showErrorGotToStart(String errorMessage) {
    if (gameController != null) {
      gameController.stopTimer();
    }
    showError(errorMessage);
    try {
      switchScene("start.fxml");
    } catch (IOException e) {
      e.printStackTrace();
    }
    client.disconnect();
  }

  @Override
  public void showError(String errorMessage) {
    Platform.runLater(() -> {
      Stage stage = new Stage();
      Parent root;
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("error.fxml"));
        root = loader.load();
        errorController = loader.getController();
        errorController.setErrorMessage(errorMessage);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        stage.showAndWait();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  @Override public void showRank(Map<Integer, Integer> finalRank) {
    Platform.runLater(() -> {
      Stage stage = new Stage();
      Parent root;
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("winner.fxml"));
        root = loader.load();
        winnerController = loader.getController();
        winnerController.setMainController(this);
        winnerController.setRank(finalRank);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        stage.showAndWait();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  void showHelpScene() {
    Platform.runLater(() -> {
      Stage stage = new Stage();
      Parent root;
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("help.fxml"));
        root = loader.load();
        stage.setScene(new Scene(root));
        stage.initOwner(primaryStage);
        stage.showAndWait();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

//  void mute() {
//    startController.muteMusic();
//  }

//  void unMute() {
//    startController.unMuteMusic();
//  }
//
//  void stopMusicInWaiting() {
//    startController.stopMusic();
//  }


//  public void setUsername(String username, int username_id) {
//    startController.setUsername(username, username_id);
//  }

//  public void setIPAddress(String ip) {
//    startController.setIpAddress(ip);
//  }

  /**
   * Sets the names of all the players in the game.
   *
   * @param names list of names, ordered clockwise
   *             The name of the recipient is on position 0
   */
  @Override
  public void setPlayerNames(List<String> names) {
    if (gameController == null) {
      waitController.setPlayerNames(names);
      return;
    }
      gameController.setPlayerNames(names);
  }

  /**
   * Sets the number of Stones each player has in its hand.
   *
   * @param sizes list of sizes, ordered clockwise
   *              The hand-size of the recipient is on position 0
   */
  @Override
  public void setHandSizes(List<Integer> sizes) {
    gameController.setHandSizes(sizes);
  }

  /**
   * Updates the table of the game-model.
   *
   * @param table the new table
   */
  @Override
  public void setTable(StoneInfo[][] table) {
    System.out.println("set table");
    gameController.setTable(table);
  }


  /**
   * Updates the hand (including the stones) of the player.
   *
   * @param hand the new hand
   */
  @Override
  public void setPlayerHand(StoneInfo[][] hand) {
    gameController.setPlayerHand(hand);
  }

  /**
   * Notifies the controller that it his his turn.
   */
  @Override
  public void notifyTurn() {
    //needed for the styling of the opponents
    gameController.notifyCurrentPlayer(0);
    gameController.yourTurn();
  }

  /**
   * Notifies the controller that the game has started.
   */
  @Override public void notifyGameStart() {
    try {
      switchToGameScene();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Notifies the controller about the player that is currently playing.
   *
   * @param playerID te player-ID who has the turn to play in the perspective of this
   */
  @Override
  public void notifyCurrentPlayer(int playerID) {
    gameController.notifyCurrentPlayer(playerID);
  }

  /**
   * Notifies the controller that his last move was invalid.
   */
  @Override
  public void notifyInvalidMove() {
    gameController.notifyInvalidMove();
//    Platform.runLater(() -> gameController.notifyInvalidMove());
  }

  /**
   * Updates the number of Stones present in the drawing-bag.
   *
   * @param bagSize number of stones in the bag
   */
  @Override
  public void setBagSize(int bagSize) {
    gameController.setBagSize(bagSize);
  }

  void sendDrawRequest() {
    client.sendRequest(new SimpleRequest(RequestID.DRAW));
  }

  void sendTimeOutRequest() {
    client.sendRequest(new SimpleRequest(RequestID.TIME_OUT));
  }

  void sendMoveSetOnTableRequest(int sourceColumn, int sourceRow, int thisColumn, int thisRow) {
    client.sendRequest(new ConcreteMove(RequestID.TABLE_SET_MOVE, sourceColumn, sourceRow, thisColumn, thisRow));
  }

  void initPlayer(String serverIP, String name, int age) {
    client = new RummiClient(serverIP);
    if (!client.isServerOK()) {
      //showError("WRONG ADDRESS YOU JACKASS");
      startController.setError("ip");
      return;
    }
    client.setGameInfoHandler(new GameInfoHandler(this));
    client.start();
    requestBuilder = new RequestBuilder(client);
    try{
      switchToWaitScene();
    } catch (IOException e) {
      e.printStackTrace();
    }
    primaryStage.setOnCloseRequest(event -> {
      if (gameController != null) {
        gameController.stopTimer();
      }
      client.disconnect();
      Platform.exit();
    });
    requestBuilder.sendSetPlayerRequest(name, age);
  }

  void startServer() {
    new RummiServer().start();
    try{
      serverIP = Inet4Address.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  void sendStartRequest() {
    requestBuilder.sendStartRequest();
  }

  void sendMoveStoneOnHand(int sourceColumn, int sourceRow, int thisColumn, int thisRow) {
    requestBuilder.moveStoneOnHand(sourceColumn, sourceRow, thisColumn, thisRow);
  }

  void sendPutStoneRequest(int sourceColumn, int sourceRow, int thisColumn, int thisRow) {
    requestBuilder.sendPutStoneRequest(sourceColumn, sourceRow, thisColumn, thisRow);
  }

  void sendMoveStoneOnTable(int sourceColumn, int sourceRow, int thisColumn, int thisRow) {
    requestBuilder.sendMoveStoneOnTable(sourceColumn, sourceRow, thisColumn, thisRow);
  }

  void sendMoveSetOnHand(int sourceColumn, int sourceRow, int thisColumn, int thisRow) {
    requestBuilder.sendMoveSetOnHand(sourceColumn, sourceRow, thisColumn, thisRow);
  }

  void sendPutSetRequest(int sourceColumn, int sourceRow, int thisColumn, int thisRow) {
    requestBuilder.sendPutSetRequest(sourceColumn, sourceRow, thisColumn, thisRow);
  }

  void sendConfirmMoveRequest() {
    requestBuilder.sendConfirmMoveRequest();
  }

  void sendSortHandByGroupRequest() {
    requestBuilder.sendSortHandByGroupRequest();
  }

  void sendSortHandByRunRequest() {
    requestBuilder.sendSortHandByRunRequest();
  }

  void quit() {
    client.disconnect();
    try {
      switchToStartScene();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  void sendResetRequest() {
    requestBuilder.sendResetRequest();
  }

  void sendUndoRequest() {
    requestBuilder.sendUndoRequest();
  }
}

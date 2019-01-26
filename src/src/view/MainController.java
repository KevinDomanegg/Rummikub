package view;

import communication.gameinfo.StoneInfo;
import communication.request.RequestID;
import communication.request.SimpleRequest;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.client.GameInfoHandler;
import network.client.RequestBuilder;
import network.client.RummiClient;
import network.server.RummiServer;
import view.music.Music;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;

public class MainController implements Controller {

  private GameController gameController;
  private StartController startController;
  private WaitController waitController;
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
    Scene scene = new Scene(root, 1024, 768);
    Platform.runLater(() -> {
      primaryStage.setScene(scene);
      primaryStage.show();
    });
  }

  private void switchToWaitScene() throws IOException {
    switchScene("wait.fxml");
    waitController.setServerIP(serverIP);
  }

  void switchToStartScene() throws IOException {
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
    if (gameController != null) {
      gameController.returnToStart(true);
    }
  }

  @Override
  public void showError(String errorMessage) {
    // switchToError
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

  void killThreads() {
    if (gameController != null) {
      gameController.stopTimer();
    }
    client.disconnect();
    System.out.println("kill client thread was pressed");
  }


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
//      gameController.notifyTurn();
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
//    Platform.runLater(() -> {
//    });

  }

  /**
   * Notifies the controller about the player that is currently playing.
   *
   * @param playerID te player-ID who has the turn to play in the perspective of this
   */
  @Override
  public void notifyCurrentPlayer(int playerID) {
    gameController.notifyCurrentPlayer(playerID);
//    Platform.runLater(() ->);
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
//      gameController.setBagSize(bagSize);
  }

  void sendDrawRequest() {
    client.sendRequest(new SimpleRequest(RequestID.DRAW));
  }

  void sendTimerRequest() {
    client.sendRequest(new SimpleRequest(RequestID.TIME_OUT));
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
    primaryStage.setOnCloseRequest(event -> client.disconnect());
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
}

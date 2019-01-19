package view;

import communication.gameinfo.StoneInfo;
import communication.request.RequestID;
import communication.request.SimpleRequest;
import network.client.RummiClient;

import java.util.List;

public class NetworkController implements Controller {

  private StartController startController;
  private GameController gameController;
  private WaitController waitController;
  private RummiClient client;

  NetworkController(String name, int age, String serverIP) {
    client = new RummiClient(name, age, serverIP);
    client.start();
  }

  void setWaitController(WaitController waitController) {
    this.waitController = waitController;
  }

  void setGameController(GameController gameController) {
    this.gameController = gameController;
  }

  /**
   * Sets the names of all the players in the game.
   *
   * @param names list of names, ordered clockwise
   *             The name of the recipient is on position 0
   */
  @Override
  public void setPlayerNames(List<String> names) {
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
    gameController.notifyTurn();
  }

  /**
   * Notifies the controller that the game has started.
   */
  @Override
  public void notifyGameStart() {
   gameController.notifyGameStart();
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

  void sendStartRequest() {
    client.sendRequest(new SimpleRequest(RequestID.START));
    // requestbuid.sfje
  }

  void sendDrawRequest() {
    client.sendRequest(new SimpleRequest(RequestID.DRAW));
  }
}

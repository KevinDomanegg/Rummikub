package view;

import communication.gameinfo.StoneInfo;
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

  /**
   * Sets the names of all the players in the game.
   *
   * @param name list of names, ordered clockwise
   *             The name of the recipient is on position 0
   */
  @Override
  public void setPlayerNames(List<String> name) {

  }

  /**
   * Sets the number of Stones each player has in its hand.
   *
   * @param sizes list of sizes, ordered clockwise
   *              The hand-size of the recipient is on position 0
   */
  @Override
  public void setHandSizes(List<Integer> sizes) {

  }

  /**
   * Updates the table of the game-model.
   *
   * @param table the new table
   */
  @Override
  public void setTable(StoneInfo[][] table) {

  }

  /**
   * Updates the hand (including the stones) of the player.
   *
   * @param hand the new hand
   */
  @Override
  public void setPlayerHand(StoneInfo[][] hand) {

  }

  /**
   * Notifies the controller that it his his turn.
   */
  @Override
  public void yourTurn() {

  }

  /**
   * Notifies the controller that the game has started.
   */
  @Override
  public void gameHasStarted() {

  }

  /**
   * Notifies the controller about the player that is currently playing.
   *
   * @param playerID
   */
  @Override
  public void currentPlayer(int playerID) {

  }

  /**
   * Notifies the controller that his last move was invalid.
   */
  @Override
  public void invalidMove() {

  }

  /**
   * Updates the number of Stones present in the drawing-bag.
   *
   * @param bagSize number of stones in the bag
   */
  @Override
  public void setBagSize(int bagSize) {

  }
}

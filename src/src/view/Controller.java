package view;

import communication.gameinfo.StoneInfo;

import java.io.IOException;
import java.util.List;

/**
 * Interface for a ShellController connecting the network to the client-model and view.
 */
public interface Controller {

  /**
   * Sets the names of all the players in the game.
   * @param names list of names, ordered clockwise
   *             The name of the recipient is on position 0
   */
  void setPlayerNames(List<String> names);

  /**
   * Sets the number of Stones each player has in its hand.
   * @param sizes list of sizes, ordered clockwise
   *              The hand-size of the recipient is on position 0
   */
  void setHandSizes(List<Integer> sizes);

  /**
   * Updates the table of the game-model.
   * @param table the new table
   */
  void setTable(StoneInfo[][] table);

  /**
   * Updates the hand (including the stones) of the player.
   * @param hand the new hand
   */
  void setPlayerHand(StoneInfo[][] hand);

  /**
   * Notifies the controller that it his his turn.
   */
  void notifyTurn();

  /**
   * Notifies the controller that the game has started.
   */
  void notifyGameStart();

  /**
   * Notifies the controller about the player that is currently playing.
   */
  void notifyCurrentPlayer(int playerID);

  /**
   * Notifies the controller that his last move was invalid.
   */
  void notifyInvalidMove();

  /**
   * Updates the number of Stones present in the drawing-bag.
   * @param bagSize number of stones in the bag
   */
  void setBagSize(int bagSize);

  void noServerAvailable();

  void showError(String errorMessage);
}

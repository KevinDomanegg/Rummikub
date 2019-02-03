package game;

import java.util.List;
import java.util.Map;

public interface Game {

  /**
   * Sets a new Player with the given name and age and tags them with
   * the given playerID.
   *
   * @param playerID the id the new Player associated to
   * @param name the name of the new Player
   * @param age the age of the new Player
   * @return true if a new Player is added in this Game
   */
  void join(int playerID, String name, int age);

  /**
   * Attempts to start this Game.
   *
   * @return true if this Game has started
   */
  void start();

  /**
   * Moves a group of stones on the Table of this Game from the given sourcePosition
   * to the given targetPosition.
   *
   * @param sourcePosition the position of a stone of moving stones before moving them
   * @param targetPosition the position of the selected stone after moving it with its group
   * @return true if subject stones are moved
   */
  boolean moveSetOnTable(Coordinate sourcePosition, Coordinate targetPosition);

  /**
   * Moves a stone at the given sourcePosition to the given targetPosition
   * on the Table of this Game.
   *
   * @param sourcePosition the position of a stone before moving it
   * @param targetPosition the position of the stone after moving it
   */
  void moveStoneOnTable(Coordinate sourcePosition, Coordinate targetPosition);

  /**
   * Puts a group of stones at the given sourcePosition from the current Hand of this Game
   * to the given targetPosition on the Table of this Game.
   *
   * @param sourcePosition the position of a stone of moving stones before moving them
   * @param targetPosition the position of the selected stone after moving it with its group
   * @return true if subject stones are moved
   */
  boolean putSet(Coordinate sourcePosition, Coordinate targetPosition);

  /**
   * Puts a stone from a Hand of this Game at the given sourcePosition to
   * the Table of this Game at the given targetPosition.
   *
   * @param sourcePosition the position of a stone before moving it
   * @param targetPosition  the position of the stone after mvoing it
   * @return true if the subject stone is moved
   */
  boolean putStone(Coordinate sourcePosition, Coordinate targetPosition);

  /**
   * Moves a group of stones on the Hand of a Player with the given playerID
   * from the given sourcePosition to the given targetPosition.
   *
   * @param playerID the id of the Player on whose Hand stones will be moved
   * @param sourcePosition the position of a stone of its group before moving them
   * @param targetPosition the position of the stone after moving it with its group
   * @return
   */
  boolean moveSetOnHand(int playerID, Coordinate sourcePosition, Coordinate targetPosition);

  /**
   * Moves a stone on the Hand of a Player with the given playerID
   * from the given sourcePosition to the given targetPosition.
   *
   * @param playerID the id of the Player on whose hand stones will be moved
   * @param sourcePosition the position of a stone before moving it
   * @param targetPosition the position of the stone after moving it
   */
  void moveStoneOnHand(int playerID, Coordinate sourcePosition, Coordinate targetPosition);

  /** Makes the current Player of this Game to draw a stone. */
  void draw(int playerID);

  /**
   * Removes a Player with the given playerID out of this Game.
   *
   * @param playerID the id of the removed Player
   */
  void removePlayer(int playerID);

  /** Resets all moves of stones that had been done on and to the Table of this Game. */
  void reset();

  /** Undoes a move of a stone that had been done on and to the Table of this Game. */
  void undo();

  /**
   * Checks if there is a winner of this Game.
   *
   * @return true if there is a winner
   */
  boolean hasWinner();

  /**
   * Checks the consistency of all moves that had been done by
   * the current Player in this Game.
   *
   * @return true if the current Player had done valid moves.
   */
  boolean isConsistent();

  /**
   * Gives all stones and their associated Coordinates on the Table of this Game.
   *
   * @return all stones and their associated Coordinates on the Table of this Game
   */
  Map<Coordinate, Stone> getTableStones();

  /**
   * Gives all stones and their associated Coordinates
   * on the Hand of a Player with the given playerID in this Game.
   *
   * @param playerID the id of the subject Player
   * @return all stones and their associated Coordinates
   * on the Hand of a Player with the given playerID in this Game
   */
  Map<Coordinate, Stone> getPlayerStones(int playerID);

  /**
   * Gives all of Players' Hand-sizes in this Game.
   *
   * @return Gives all of Players' Hand-sizes in this Game
   */
  List<Integer> getPlayerHandSizes();

  int getBagSize();

  int getCurrentPlayerID();

  int getTableWidth();

  int getTableHeight();

  int getPlayerHandWidth(int playerID);

  int getPlayerHandHeight(int playerID);

  int getNumberOfPlayers();

  // TODO: javadoc
  Map<Integer, Integer> getFinalRank();

  List<String> getPlayerNames();

  void sortPlayerHandByGroup(int playerID);

  void sortPlayerHandByRun(int playerID);

  boolean hasPlayerPlayedFirstMove(int playerID);
  
  /**
   * Draws a Stone when if there are Stones available.
   * Switches to the next player if not.
   */
  void timeOut(int playerID);
  boolean isGameOn();
}

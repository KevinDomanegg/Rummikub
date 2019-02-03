package game;

import java.util.List;
import java.util.Map;

/** An abstract Game for Rummikub. */
public interface Game {

  /**
   * Sets a new Player with the given name and age and tags them with
   * the given playerId.
   *
   * @param playerId the id the new Player associated to
   * @param name the name of the new Player
   * @param age the age of the new Player
   * @return true if a new Player is added in this Game
   */
  boolean setPlayer(int playerId, String name, int age);

  /**
   * Attempts to start this Game.
   *
   * @return true if this Game has started
   */
  boolean start();

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
   * Moves a group of stones on the Hand of a Player with the given playerId
   * from the given sourcePosition to the given targetPosition.
   *
   * @param playerId the id of the Player on whose Hand stones will be moved
   * @param sourcePosition the position of a stone of its group before moving them
   * @param targetPosition the position of the stone after moving it with its group
   * @return true if only if the stones are moved
   */
  boolean moveSetOnHand(int playerId, Coordinate sourcePosition, Coordinate targetPosition);

  /**
   * Moves a stone on the Hand of a Player with the given playerId
   * from the given sourcePosition to the given targetPosition.
   *
   * @param playerId the id of the Player on whose hand stones will be moved
   * @param sourcePosition the position of a stone before moving it
   * @param targetPosition the position of the stone after moving it
   */
  void moveStoneOnHand(int playerId, Coordinate sourcePosition, Coordinate targetPosition);

  /** Makes the current Player of this Game to draw a stone. */
  void drawStone();

  /**
   * Removes a Player with the given playerId out of this Game.
   *
   * @param playerId the id of the removed Player
   */
  void removePlayer(int playerId);

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
   * on the Hand of a Player with the given playerId in this Game.
   *
   * @param playerId the id of the subject Player
   * @return all stones and their associated Coordinates
   *         on the Hand of a Player with the given playerId in this Game
   */
  Map<Coordinate, Stone> getPlayerStones(int playerId);

  /**
   * Gives all of Players' Hand-sizes in this Game.
   *
   * @return Gives all of Players' Hand-sizes in this Game
   */
  List<Integer> getPlayerHandSizes();

  /**
   * Returns the size of the Bag in this Game.
   *
   * @return the size of the Bag in this Game.
   */
  int getBagSize();

  int getCurrentPlayerId();

  int getTableWidth();

  int getTableHeight();

  int getPlayerHandWidth(int playerId);

  int getPlayerHandHeight(int playerId);

  int getNumberOfPlayers();

  /**
   * Returns all names of players with their points sorted by their points.
   *
   * @return all names of players with their points sorted by their points
   */
  Map<String, Integer> getFinalRank();

  /**
   * Returns names of players in this Game in order of their IDs.
   *
   * @return names of players in this Game
   */
  List<String> getPlayerNames();

  /**
   * Sorts stones on the Hand of the Player with the given playerId for the Set type, the Group.
   * Group is a group of (3 or 4) stones that have the same number but different colors.
   *
   * @param playerId the ID of the Player whose Hand will be sorted
   */
  void sortPlayerHandByGroup(int playerId);

  /**
   * Sorts stones on the Hand of the Player with the given playerId for the Set type, the Run.
   * Run is a group of at least 3 ordered stones by their number that have the same color.
   * For example: (13, 1, 2) is considered an ordered sequence.
   *
   * @param playerId the ID of the Player whose Hand will be sorted
   */
  void sortPlayerHandByRun(int playerId);

  /**
   * Returns true if only if the Player with the given playerId has played their first move.
   *
   * @param playerId the id of the subject Player
   * @return true if only if the Player with the given playerId has played their first move
   */
  boolean hasPlayerPlayedFirstMove(int playerId);

  /**
   * Returns true if this Game is on going.
   *
   * @return true if this Game is on going
   */
  boolean isGameOn();
  /**
   * Draws a Stone when if there are Stones available.
   * Switches to the next player if not.
   */
  void timeOut();
}

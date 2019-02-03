package game;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

import globalconstants.Constants;
import globalconstants.ErrorMessages;

/**
 * Model for the board game Rummikub.
 * Handles all the game-logic as well as the saving of all relevant data.
 */
public class RummiGame implements Game {

  private RummiTable table;
  private HashMap<Integer, Player> players;
  private RummiBag bag;
  private Stack<Trace> trace;
  private boolean isGameOn;
  private int currentPlayerID;
  private int pointsBeforeTurn;
  private int pointsAfterTurn;


  /**
   * Initializes the RummiGame for Rummikub board game.
   * For this Game a Table (RummiTable) and Players (with their RummiHands) will be
   * first initialized. For the purpose of reset the move data will be initialized.
   */
  public RummiGame() {
    table = new RummiTable();
    players = new HashMap<>(Constants.MAX_PLAYERS);
    trace = new Stack<>();
  }

  /**
   * Takes a Stone from the Bag and gives it to the current Player.
   *
   * @throws UnsupportedOperationException If the Bag is empty or the Hand is full.
   */
  private void giveStoneToPlayer(int playerID) throws UnsupportedOperationException {
    Player player = players.get(playerID);

    if (isGameOn == false) {
      throw new UnsupportedOperationException(ErrorMessages.GAME_DID_NOT_START_YET_ERROR);
    }

    if (player.getHandSize() >= Constants.MAX_HAND_SIZE) {
      throw new UnsupportedOperationException(ErrorMessages.HAND_IS_FULL_ERROR);
    }
    if (bag.size() < 1) {
      throw new UnsupportedOperationException(ErrorMessages.BAG_IS_EMPTY_ERROR);
    }
    player.pushStone(bag.removeStone());

  }

  /**
   * Updates the currentPlayerID.
   */
  private void nextTurn() throws UnsupportedOperationException {
    if (isGameOn == false) {
      throw new UnsupportedOperationException(ErrorMessages.GAME_DID_NOT_START_YET_ERROR);
    }

    // the ID of the current player will be updated (0 follows after 3)
    do {
      currentPlayerID = (currentPlayerID + 1) % Constants.MAX_PLAYERS;
    } while (!players.containsKey(currentPlayerID));

    pointsBeforeTurn = getCurrentPlayer().points();
    System.out.println(getCurrentPlayer().getHand());

  }

  /**
   * Draws a Stone and selects the next player.
   *
   * @param playerID of the Player who wants to make this move
   * @throws UnsupportedOperationException if the Hand is full, the bag is empty or its not the players turn.
   */

  public void draw(int playerID) throws UnsupportedOperationException {
    if (playerID != currentPlayerID) {
      throw new UnsupportedOperationException(ErrorMessages.NOT_YOUR_TURN_ERROR);
    }
      giveStoneToPlayer(playerID);
      nextTurn();
  }

  /**
   * Adds a new player with the given playerID, name and age in this game before the game start.
   *
   * @param playerID the id of the player
   * @param name     the name of the player
   * @param age      the age of the player
   * @return false if only if there are already 4 players or the game is on
   */
  @Override
  public void join(int playerID, String name, int age) {
    if(isGameOn) {
      throw new UnsupportedOperationException(ErrorMessages.GAME_HAS_ALREADY_STARTED_ERROR);
    }
    if (players.size() >= Constants.MAX_PLAYERS) {
      throw new UnsupportedOperationException(ErrorMessages.GAME_IS_FULL_ERROR);
    }

    players.put(playerID, new Player(name, age));
  }

  /**
   * Starts the game by handing out stones and determining the start player.
   *
   * @return false if only if this game has already started
   */
  @Override
  public void start() throws UnsupportedOperationException {
    if (isGameOn == true) {
      throw new UnsupportedOperationException(ErrorMessages.GAME_HAS_ALREADY_STARTED_ERROR);
    }
    if (players.size() < Constants.MIN_PLAYERS) {
      throw new UnsupportedOperationException(ErrorMessages.NOT_ENOUGH_PLAYERS_ERROR);
    }
    if (players.size() >= Constants.MAX_PLAYERS) {
      throw new UnsupportedOperationException(ErrorMessages.GAME_IS_FULL_ERROR);
    }
    isGameOn = true;
    bag = new RummiBag();
    table.clear();
    handOutStones();
    setStartPlayer();

    pointsBeforeTurn = getCurrentPlayer().points();
  }

  /**
   * Hand out first stones randomly to each player.
   */
  private void handOutStones() {
    // clear players' hands first
    for (Player player : players.values()) {
      player.clearHand();
    }
    // hand out stones
    for (int i = 0; i < Constants.FIRST_STONES; i++) {
      for (int j = 0; j < players.size(); j++) {
        giveStoneToPlayer(j);
      }
    }
  }

  /**
   * Sets the youngest player as starter.
   */
  private void setStartPlayer() {
    int minAge = Integer.MAX_VALUE;
    int age;
    for (Entry<Integer, Player> entry : players.entrySet()) {
      if ((age = entry.getValue().getAge()) < minAge) {
        minAge = age;
        currentPlayerID = entry.getKey();
      }
    }
  }

  /**
   * Moves neighbor stones at the given sourcePosition on the given sourceGrid
   * to the given targetPosition on the given targetGrid with the method of the given StoneMove.
   * If there are other (not neighbor) stones around the given targetPosition,
   * targetGrid stays unchanged. The positions of the subject stone and its neighbors after moving
   * them will be automatically suited on the given targetGrid if their positions are acceptable.
   *
   * @param sourcePosition the position of the subject stone before moving it with neighbors
   * @param targetPosition the position of the subject stone after moving it with neighbors
   * @param sourceGrid     the grid where the subject stone and its neighbors come from
   * @param targetGrid     the grid where the subject stone and its neighbors move to
   * @param stoneMove      the container of a method to be used for moving
   * @return true if only if the set (neighbored stones) is successfully moved
   */
  private static boolean moveSet(Coordinate sourcePosition, Coordinate targetPosition, Grid sourceGrid, Grid targetGrid, StoneMove stoneMove) {
    Map<Coordinate, Stone> sourceStones = sourceGrid.getStones();
    // check if there is a stone on the sourceGrid at the given sourcePosition
    if (!sourceStones.containsKey(sourcePosition)) {
      return false;
    }
    Coordinate coordinate = sourceGrid.getFirstCoordOfStonesAt(sourcePosition);
    int srcCol = coordinate.getCol();
    int srcRow = coordinate.getRow();
    int trgCol = targetPosition.getCol() - (sourcePosition.getCol() - srcCol);
    int trgRow = targetPosition.getRow();
    HashSet<Coordinate> neighborCoordinates = new HashSet<>(); // coordinates of stones to be moved
    // save all coordinates of neighbor stones at the sourcePosition
    for (int i = 0; sourceStones.containsKey(coordinate); i++) {
      neighborCoordinates.add(coordinate);
      coordinate = new Coordinate(srcCol + i, srcRow);
    }
    int setSize = neighborCoordinates.size();
    // make trgCol suitable on targetGrid
    trgCol = Math.min(Math.max(0, trgCol), targetGrid.getWidth() - setSize);
    Map<Coordinate, Stone> targetStones = targetGrid.getStones();
    // check if it is safe to move stones on targetGrid
    for (int i = 0; i < setSize; i++) {
      coordinate = new Coordinate(trgCol + i, trgRow);
      // check if the coordinate on target grid is clear or the source- and targetGrid are the same
      if (targetStones.containsKey(coordinate)
              // and the coordinate is a part of neighborCoordinates of moving stones
              && !(sourceGrid.equals(targetGrid) && neighborCoordinates.contains(coordinate))) {
        return false;
      }
    }
    // check if targetPosition was at the right side of the sourcePosition
    if (trgCol > srcCol) {
      // move stones starting from right
      for (int i = setSize - 1; i >= 0; i--) {
        stoneMove.moveStone(new Coordinate(srcCol + i, srcRow), new Coordinate(trgCol + i, trgRow));
      }
    } else {
      // move stones starting from left
      for (int i = 0; i < setSize; i++) {
        stoneMove.moveStone(new Coordinate(srcCol + i, srcRow), new Coordinate(trgCol + i, trgRow));
      }
    }
    return true;
  }

  /**
   * Checks if is safe to move stones with the given stoneSize to the given
   * targetStones at the given trgCol and trgRow. If the given sourceIsTarget is
   * true and the given srcRow and trgRow are the same (they are at the same Row),
   * then check if there are any stones at the subject coordinates apart from
   * the coordinates that share the same srcCol and trgCol.
   * The target is safe, if there are no stone at the target coordinates (apart from
   * the shared coordinates if source is target.
   *
   * @param srcCol the column of the source Coordinate used to shift the trgCol to be checked
   * @param srcRow the row of the source Coordinate compared with the given trgRow
   * @param trgCol the column of the target Coordinate to be checked
   * @param trgRow the row of the target Coordinate compared with the given srcRow
   * @param targetStones the stones from the target
   * @param stoneSize the size of stones to be checked
   * @return true if only if it is safe to move stones to the target
   */
  private static boolean isTargetSafe(int srcCol, int srcRow, int trgCol, int trgRow,
      boolean sourceIsTarget, Map<Coordinate, Stone> targetStones, int stoneSize) {
    if (sourceIsTarget && srcRow == trgRow) {
      int s = Math.min(Math.abs(srcCol - trgCol), stoneSize);
      trgCol += (trgCol > srcCol) ? stoneSize - s : 0;
      stoneSize = s;
    }
    for (int i = 0; i < stoneSize; i++) {
      if (targetStones.containsKey(new Coordinate(trgCol + i, trgRow))) {
        return false;
      }
    }
    return true;
  }


  /**
   * Moves a stone with its neighbor stones from the given sourcePosition at this table
   * to the given targetPosition at this table.
   *
   * @param sourcePosition the position of the subject stone before moving it with its neighbors
   * @param targetPosition the position of the subject stone after moving it with its neighbors
   * @return true if only if the set (neighbored stones) is successfully moved
   */
  @Override
  public boolean moveSetOnTable(Coordinate sourcePosition, Coordinate targetPosition) {
    return moveSet(sourcePosition, targetPosition, table, table, this::moveStoneOnTable);
  }

  /**
   * Moves stone from the given sourcePosition to the given targetPosition on the table.
   * If a stone at the targetPosition already exist, it will be swapped.
   * Hereby, this move will be stored in the trace for reset.
   *
   * @param sourcePosition the position of the subject stone before moving
   * @param targetPosition the position of the subject stone after moving
   */
  @Override
  public void moveStoneOnTable(Coordinate sourcePosition, Coordinate targetPosition) {
    swapStoneOnTable(sourcePosition, targetPosition);
    // store this move.
    trace.push(new Trace(TraceMove.MOVE_STONE_ON_TABLE, sourcePosition, targetPosition));
  }

  /**
   * Swaps stones between the given sourcePosition to the given targetPosition on the table.
   *
   * @param sourcePosition the position of a stone or null to be swapped
   * @param targetPosition the position of a stone or null to be swapped
   */
  private void swapStoneOnTable(Coordinate sourcePosition, Coordinate targetPosition) {
    // save stone for swap
    Stone chosenStone = table.removeStone(sourcePosition);
    // move stone from targetPosition to sourcePosition
    table.setStone(sourcePosition, table.removeStone(targetPosition));
    // move the chosen stone to targetPosition
    table.setStone(targetPosition, chosenStone);
    // it seems like maps are not deleting value pairs. The just overwrite the value with null.
    table.getStones().values().removeIf(Objects::isNull);
  }

  /**
   * Puts a stone with its neighbor stones from the given sourcePosition at this table
   * to the given targetPosition at this hand.
   *
   * @param sourcePosition the position of the subject stone before moving it with its neighbors
   * @param targetPosition the position of the subject stone after moving it with its neighbors
   * @return true if only if the set (neighbored stones) is successfully moved
   */
  @Override
  public boolean putSet(Coordinate sourcePosition, Coordinate targetPosition) {
    return moveSet(sourcePosition, targetPosition, currentPlayer().getHand(), table, this::putStone);
  }


  private void moveStoneToTable(Coordinate sourcePosition, Coordinate targetPosition) throws UnsupportedOperationException{
    if (table.getStones().containsKey(targetPosition)) {
      throw new UnsupportedOperationException(ErrorMessages.SPOT_ALREADY_TAKEN_ERROR);
    }
    Stone movingStone = currentPlayer().popStone(sourcePosition);
    table.setStone(targetPosition, movingStone);
  }

  /**
   * Puts a stone from the current player hand to the table if the target position is empty.
   * Hereby, this move will be stored in the trace for reset.
   *
   * @param sourcePosition the position of the subject stone before putting
   * @param targetPosition the position of the subject stone after putting
   * @return true if only if a stone from sourcePosition is to targetPosition moved
   */
  @Override
  public void putStone(Coordinate sourcePosition, Coordinate targetPosition) throws UnsupportedOperationException {
    // check if target position is empty

    moveStoneToTable(sourcePosition, targetPosition);

    trace.push(new Trace(TraceMove.MOVE_STONE_FROM_HAND, sourcePosition, targetPosition));
  }

  /**
   * Moves a stone with its neighbor stones from the given sourcePosition at this Hand
   * to the given targetPosition at this Hand.
   *
   * @param sourcePosition the position of the subject stone before moving it with its neighbors
   * @param targetPosition the position of the subject stone after moving it with its neighbors
   * @return true if only if the set (neighbored stones) is successfully moved
   */
  @Override
  public boolean moveSetOnHand(int playerID, Coordinate sourcePosition, Coordinate targetPosition) {
    Grid hand = players.get(playerID).getHand();
    return moveSet(sourcePosition, targetPosition, hand, hand,
            (srcPos, trgPos) -> moveStoneOnHand(playerID, srcPos, trgPos));
  }

  /**
   * Moves stone from the given sourcePosition to the given targetPosition
   * on the players Hand with the ID of the given playerId.
   * If the player with the given playerId is the current player ID,
   * this move will be stored in the moveData for reset.
   *
   * @param playerID       the ID of the player who moves the stone on their hand
   * @param sourcePosition the position of the subject stone before moving
   * @param targetPosition the position of the subject stone after moving
   */
  @Override
  public void moveStoneOnHand(int playerID, Coordinate sourcePosition, Coordinate targetPosition) {
    players.get(playerID).moveStone(sourcePosition, targetPosition);
  }



  /**
   * Removes the player with the given playerId
   * out of this game and reset their stones into the bag.
   *
   * @param playerID the ID of the player who left
   */
  @Override
  public void removePlayer(int playerID) throws UnsupportedOperationException {
    System.out.println("---number of players: " + players.size());
    if (isGameOn == false) {
      players.remove(playerID);
      return;
    }
    // remove the player with the playerID and reset their hand into the bag
    bag.addStones(players.remove(playerID).getStones().values());
    if (players.size() < Constants.MIN_PLAYERS) {
      isGameOn = false;
      return;
    }
    if (currentPlayerID == playerID) {
      nextTurn();
    }
  }

  /**
   * Resets all moves of the current player on this table and from their hand.
   */
  @Override
  public void reset() {
    while (!trace.empty()) {
      undo();
    }
  }

  /**
   * Undoes the last move of the current player.
   */
  @Override
  public void undo() {
    if (trace.empty()) {
      return;
    }

    Trace lastCommand = trace.pop();
    Coordinate sourcePosition = lastCommand.getInitialPosition();
    Coordinate targetPosition = lastCommand.getTargetPosition();
    TraceMove command = lastCommand.getCommand();

    switch (command) {
      case MOVE_STONE_ON_TABLE:
        // swap back stones on the table
        swapStoneOnTable(targetPosition, sourcePosition);
        return;
      case MOVE_STONE_FROM_HAND:
        // get back stone from the table to the player hand
        Stone stone = table.removeStone(targetPosition);
        //currentPoints -= stone.getNumber();
        currentPlayer().pushStone(stone);
        return;
    }
  }

  /**
   * Checks if the current player has no stone on hand and gives the result.
   *
   * @return true if only if the current player has won this game
   */
  @Override
  public boolean hasWinner() {
    return currentPlayer().getHandSize() == 0;
  }

  private void setPlayerFree() {
    pointsAfterTurn = getCurrentPlayer().points();
    System.out.println(getCurrentPlayer().getHand());

    System.out.println("PointsAfterTurn" + pointsAfterTurn);
    System.out.println("PointsBeforeTurn " + pointsBeforeTurn);
    System.out.println("Points " + (pointsBeforeTurn - pointsAfterTurn));
    if( (pointsBeforeTurn - pointsAfterTurn) >= Constants.MIN_FIRST_MOVE_POINTS){
      getCurrentPlayer().setPlayedFirstMove(true);
    } else if((pointsBeforeTurn - pointsAfterTurn) == 0){
      giveStoneToPlayer(currentPlayerID);
    } else {
      throw new UnsupportedOperationException(ErrorMessages.NOT_ENOUGH_POINTS_ERROR);
    }
  }


  /**
   * Checks the consistency of all of moves the current Player did.
   * This Game is consistent, if the current Player has played at least a Stone from their Hand
   * and played total 30 points of stones from Hand if it was their first turn
   * and the Table is consistent.
   *
   * @return false if only if the current player's move is first but points are lower than 30
   * or the played table is not consistent
   */

  public void confirmMove(int playerID) {
    if (playerID != currentPlayerID) {
      throw new UnsupportedOperationException(ErrorMessages.NOT_YOUR_TURN_ERROR);
    }
    pointsAfterTurn = getCurrentPlayer().points();
    int pointsPlayed = (pointsBeforeTurn - pointsAfterTurn);
    if (pointsPlayed == 0) {
      //TODO: make new Exception for 0 points
      throw new UnsupportedOperationException(ErrorMessages.NOT_ENOUGH_POINTS_ERROR);
    }
//    if (!currentPlayer().hasPlayedFirstMove() && pointsPlayed < Constants.MIN_FIRST_MOVE_POINTS) {
//      throw new UnsupportedOperationException(ErrorMessages.NOT_ENOUGH_POINTS_ERROR);
//    }
//    if(table.isConsistent() == false) {
//      throw new UnsupportedOperationException(ErrorMessages.TABLE_NOT_CONSISTENT_ERROR);
//    }
    currentPlayer().setPlayedFirstMove(true);
    if(hasWinner()){
      isGameOn = false;
    } else {
      nextTurn();
      trace.clear();
    }


  }



  @Override
  public boolean isConsistent() {
    return table.isConsistent();
  }

  @Override
  public Map<Coordinate, Stone> getTableStones() {
    return table.getStones();
  }

  @Override
  public Map<Coordinate, Stone> getPlayerStones(int playerID) {
    return players.get(playerID).getStones();
  }

  @Override
  public int getBagSize() {
    return bag.size();
  }

  @Override
  public List<Integer> getPlayerHandSizes() {
    return players.values().stream().map(Player::getHandSize).collect(Collectors.toList());
  }

  @Override
  public List<String> getPlayerNames() {
    return players.values().stream().map(Player::getName).collect(Collectors.toList());
  }

  /**
   * sorts stones on the hand of the player with the given playerID by group-set.
   *
   * @param playerID the id of the player whose hand will be sorted
   */
  @Override
  public void sortPlayerHandByGroup(int playerID) {
    players.get(playerID).sortHandByGroup();
  }

  /**
   * sorts stones on the hand of the player with the given playerID by run-set.
   *
   * @param playerID the id of the player whose hand will be sorted
   */
  @Override
  public void sortPlayerHandByRun(int playerID) {
    players.get(playerID).sortHandByRun();
  }

  @Override
  public boolean hasPlayerPlayedFirstMove(int playerID) {
    return players.get(playerID).hasPlayedFirstMove();
  }

  @Override
  public boolean isGameOn() {
    return isGameOn;
  }

  @Override
  public int getCurrentPlayerID() {
    return currentPlayerID;
  }

  @Override
  public int getTableWidth() {
    return table.getWidth();
  }

  @Override
  public int getTableHeight() {
    return table.getHeight();
  }

  @Override
  public int getPlayerHandWidth(int playerID) {
    return players.get(playerID).getHandWidth();
  }

  @Override
  public int getPlayerHandHeight(int playerID) {
    return players.get(playerID).getHandHeight();
  }

  @Override
  public int getNumberOfPlayers() {
    return players.size();
  }

  /**
   * Returns all Players' names wih their points sorted by their points.
   *
   * @return the sorted Players' names with their points.
   */
  @Override public Map<String, Integer> getFinalRank() {
    Map<String, Integer> rank = new HashMap<>(players.size());
    players.values().stream()
        .sorted(Comparator.comparingInt(Player::getPoints).reversed())
        .forEach((player) -> rank.put(player.getName(), player.getPoints()));
    return rank;
  }

  // for test

  Stack<Trace> getTrace() {
    return trace;
  }

  public Player getCurrentPlayer() {
    return players.get(currentPlayerID);
  }

  /**
   * Draws a Stone when if there are Stones available.
   * Switches to the next player if not.
   */
  @Override
  public void timeOut(int playerID) {
    if (!isGameOn) {
      return;
    }
    try {
      reset();
      draw(playerID);
    } catch (UnsupportedOperationException e) {
      nextTurn();
    }
  }

  /**
   * Gives a current player.
   */
  private Player currentPlayer() {
    return players.get(currentPlayerID);
  }
}

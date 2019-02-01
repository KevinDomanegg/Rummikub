package game;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Model for the board game Rummikub.
 */
public class RummiGame implements Game {
  private static final int MIN_PLAYERS = 2;
  private static final int MAX_PLAYERS = 4;
  // the number of stones players receive at the beginning
  private static final int FIRST_STONES = 14;
  // the minimal points every player should make for their first move
  private static final int MIN_FIRST_MOVE_POINTS = 30;

  private RummiTable table; // table of the game
  private HashMap<Integer, Player> players;
  private RummiBag bag; // bag where all stones are filled
  private Stack<MoveTrace> trace; // history of the current players each move
  private boolean isGameOn;
  private int currentPlayerID;
  private int currentPoints; // the points of the first move of a current player

  public RummiGame() {
    table = new RummiTable();
    players = new HashMap<>(MAX_PLAYERS);
    trace = new Stack<>();
  }

  /** Gives a current player. */
  private Player currentPlayer() {
    return players.get(currentPlayerID);
  }

  /** Updates the currentPlayerID. */
  private void nextTurn() {
    if (!isGameOn) {
      System.out.println("GAME NOT ON!");
      return;
    }
    // reset currentPoints
    currentPoints = 0;
    // the ID of the current player will be updated (0 follows after 3)
    do {
      currentPlayerID = (currentPlayerID + 1) % MAX_PLAYERS;
    } while (!players.containsKey(currentPlayerID));
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
  public boolean setPlayer(int playerID, String name, int age) {
    if (players.size() > MAX_PLAYERS || isGameOn) {
      return false;
    }
    players.put(playerID, new Player(name, age));
    return true;
  }

  /**
   * Starts the game by handing out stones and determining the start player.
   *
   * @return false if only if this game has already started
   */
  @Override
  public boolean start() {
    if (isGameOn) {
      return false;
    }
    if (players.size() >= MIN_PLAYERS) {
      isGameOn = true;
      bag = new RummiBag();
      table.clear();
      handOutStones();
      setStartPlayer();
    }
    return true;
  }

  /** Hand out first stones randomly to each player. */
  private void handOutStones() {
    // clear players' hands first
    for (Player player : players.values()) {
      player.clearHand();
    }
    // hand out stones
    for (int i = 0; i < FIRST_STONES; i++) {
      for (int j = 0; j < players.size(); j++) {
        drawStone();
      }
    }
  }

  /** Sets the youngest player as starter. */
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
    Map<Coordinate, Stone>  sourceStones = sourceGrid.getStones();
    // check if there is a stone on the sourceGrid at the given sourcePosition
    if (!sourceStones.containsKey(sourcePosition)) {
      return false;
    }
    Coordinate coordinate = sourceGrid.getFirstCoordOfSetAt(sourcePosition);
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
    trace.push(new MoveTrace("MOVESTONEONTABLE", sourcePosition, targetPosition));
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
    return
        moveSet(sourcePosition, targetPosition, currentPlayer().getHand(), table, this::putStone);
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
  public boolean putStone(Coordinate sourcePosition, Coordinate targetPosition) {
    // check if target position is empty
    if (table.getStones().containsKey(targetPosition)) {
      return false;
    }
    Stone movingStone = currentPlayer().popStone(sourcePosition);
    if (movingStone == null) {
      return false;
    }
    // add up the currentPoints
    currentPoints += movingStone.getNumber();
    table.setStone(targetPosition, movingStone);
    // store this move
    trace.push(new MoveTrace("MOVESTONEFROMHAND", sourcePosition, targetPosition));
    return true;
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
   * on the players Hand with the ID of the given playerID.
   * If the player with the given playerID is the current player ID,
   * this move will be stored in the trace for reset.
   *
   * @param playerID       the ID of the player who moves the stone on their hand
   * @param sourcePosition the position of the subject stone before moving
   * @param targetPosition the position of the subject stone after moving
   */
  @Override
  public void moveStoneOnHand(int playerID, Coordinate sourcePosition, Coordinate targetPosition) {
    players.get(playerID).moveStone(sourcePosition, targetPosition);
//    if (playerID == currentPlayerID){
//      // store this move
//      trace.push(new MoveTrace("MOVESTONEONHAND", sourcePosition, targetPosition));
//    }
  }

  /**
   * Makes the current player draw a stone from the bag and finish their turn.
   */
  @Override
  public void drawStone() {
    currentPlayer().pushStone(bag.removeStone());
    nextTurn();
  }


  /**
   * Kicks the player with the given playerID out of this game and reset their stones into the bag.
   *
   * @param playerID the ID of the player who left
   */
  @Override public void removePlayer(int playerID) {
    System.out.println("---number of players: " + players.size());
    if (!isGameOn) {
      players.remove(playerID);
      return;
    }
    // remove the player with the playerID and reset their hand into the bag
    bag.addStones(players.remove(playerID).getStones().values());
    if (players.size() < MIN_PLAYERS) {
      isGameOn = false;
      return;
    }
    if (currentPlayerID == playerID) {
      nextTurn();
    }
  }

  /** Resets all moves of the current player on this table and from their hand. */
  @Override public void reset() {
    while (!trace.empty()) {
      undo();
    }
//    currentPoints = 0;
  }

  /** Undoes the last move of the current player. */
  @Override public void undo() {
    if (trace.empty()){
      return;
    }

    MoveTrace lastCommand = trace.pop();
    Coordinate sourcePosition = lastCommand.getInitialPosition();
    Coordinate targetPosition = lastCommand.getTargetPosition();
    String command = lastCommand.getCommand();

    switch (command) {
      case "MOVESTONEONTABLE":
        // swap back stones on the table
        swapStoneOnTable(targetPosition, sourcePosition);
        return;
      case "MOVESTONEFROMHAND":
        // get back stone from the table to the player hand
        Stone stone = table.removeStone(targetPosition);
        currentPoints -= stone.getNumber();
        currentPlayer().pushStone(stone);
        return;
      default:
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

  /**
   * checks if the current player has already played their first move with points of 30
   * and then the consistency of the played table.
   * If the game is not consistent, it will be reset to the game before the current player's moves.
   *
   * @return false if only if the current player's move is first but points are lower than 30
   * or the played table is not consistent
   */
  @Override
  public boolean isConsistent() {
    // check if the current player has played something yet
    if (currentPoints == 0) {
      return false;
    }
    // check if the current player has played their (first) turn in this game
    if (/*!currentPlayer().hasPlayedFirstMove() && currentPoints < MIN_FIRST_MOVE_POINTS || */!table.isConsistent()) {
      return false;
    }
    // clear the trace for the next turn
    trace.clear();
    currentPlayer().playedFirstMove();
    // check if this player has won
    if (currentPlayer().getHandSize() == 0) {
      isGameOn = false;
    }
    nextTurn();
    return true;
  }

  @Override public Map<Coordinate, Stone> getTableStones() {
    return table.getStones();
  }

  @Override public Map<Coordinate, Stone> getPlayerStones(int playerID) {
    return players.get(playerID).getStones();
  }

  @Override public int getBagSize() {
    return bag.size();
  }

  @Override public List<Integer> getPlayerHandSizes() {
    return players.values().stream().map(Player::getHandSize).collect(Collectors.toList());
  }

  @Override public List<String> getPlayerNames() {
    return players.values().stream().map(Player::getName).collect(Collectors.toList());
  }

  /**
   * sorts stones on the hand of the player with the given playerID by group-set.
   *
   * @param playerID the id of the player whose hand will be sorted
   */
  @Override public void sortPlayerHandByGroup(int playerID) {
    players.get(playerID).sortHandByGroup();
  }

  /**
   * sorts stones on the hand of the player with the given playerID by run-set.
   *
   * @param playerID the id of the player whose hand will be sorted
   */
  @Override public void sortPlayerHandByRun(int playerID) {
    players.get(playerID).sortHandByRun();
  }

  @Override public boolean hasPlayerPlayedFirstMove(int playerID) {
    return players.get(playerID).hasPlayedFirstMove();
  }

  @Override public int getCurrentPlayerID() {
    return currentPlayerID;
  }

  @Override public int getTableWidth() {
    return table.getWidth();
  }

  @Override public int getTableHeight() {
    return table.getHeight();
  }

  @Override public int getPlayerHandWidth(int playerID) {
    return players.get(playerID).getHandWidth();
  }

  @Override public int getPlayerHandHeight(int playerID) {
    return players.get(playerID).getHandHeight();
  }

  @Override public int getNumberOfPlayers() {
    return players.size();
  }
  // for test

  Stack<MoveTrace> getTrace() {
    return trace;
  }

  /**
   * gives the list of entries (key: playerID -> value: playerPoints) sorted by their value.
   *
   * @return the sorted (by values (points) list
   */
  @Override public Map<Integer, Integer> getFinalRank() {
    List<Entry<Integer, Integer>> rank = players.entrySet().stream()
        .map((entry) -> new SimpleEntry<>(entry.getKey(), entry.getValue().getPoints()))
        .sorted(Comparator.comparing(Entry::getValue)).collect(Collectors.toList());
    return rank.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }
}

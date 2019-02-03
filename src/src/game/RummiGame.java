package game;

import game.MoveTrace.Move;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Model for the board game Rummikub.
 * Handles all the game-logic as well as the saving of all relevant data.
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
  private Stack<MoveTrace> trace; // history of the current Player's each move
  private boolean gameOn; // the state of game that tells if it is on going or not
  private int currentPlayerId; // the id of the current player
  private int currentPoints; // the points of the first move of a current player

  /**
   * Initializes the RummiGame for Rummikub board game.
   * For this Game a Table (RummiTable) and Players (with their RummiHands) will be
   * first initialized. For the purpose of reset the move data will be initialized.
   */
  public RummiGame() {
    table = new RummiTable();
    players = new HashMap<>(MAX_PLAYERS);
    trace = new Stack<>();
  }

  public RummiTable getTable() {
    return table;
  }

  /** Gives a current player. */
  private Player currentPlayer() {
    return players.get(currentPlayerId);
  }

  /** Updates the currentPlayerId. */
  private void nextTurn() {
    if (!gameOn) {
      System.out.println("GAME NOT ON!");
      return;
    }
    // reset currentPoints
    currentPoints = 0;
    // the ID of the current player will be updated (0 follows after 3)
    do {
      currentPlayerId = (currentPlayerId + 1) % MAX_PLAYERS;
    } while (!players.containsKey(currentPlayerId));
  }

  /**
   * Adds a new player with the given playerId, name and age in this game before the game start.
   *
   * @param playerId the id of the player
   * @param name     the name of the player
   * @param age      the age of the player
   * @return false if only if there are already 4 players or the game is on
   */
  @Override
  public boolean setPlayer(int playerId, String name, int age) {
    if (players.size() > MAX_PLAYERS || gameOn) {
      return false;
    }
    players.put(playerId, new Player(name, age));
    return true;
  }

  /**
   * Starts the game by handing out stones and determining the start player.
   *
   * @return false if only if this game has already started
   */
  @Override
  public boolean start() {
    if (gameOn) {
      return false;
    }
    if (players.size() >= MIN_PLAYERS) {
      gameOn = true;
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
        currentPlayerId = entry.getKey();
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
  private static boolean moveSet(Coordinate sourcePosition, Coordinate targetPosition,
      Grid sourceGrid, Grid targetGrid, StoneMove stoneMove) {
    // check if there is a stone on the given sourceGrid at the given sourcePosition
    if (!sourceGrid.getStones().containsKey(sourcePosition)) {
      return false;
    }
    Coordinate firstCoord = sourceGrid.getFirstCoordOfStonesAt(sourcePosition);
    int srcCol = firstCoord.getCol();
    int srcRow = firstCoord.getRow();
    // shift trgCol like srcCol did by getting first coordinate
    int trgCol = targetPosition.getCol() + srcCol - sourcePosition.getCol();
    int trgRow = targetPosition.getRow();
    int setSize = sourceGrid.getNeighborStonesSize(firstCoord); // the size of a potential set
    // make trgCol suitable on the given targetGrid
    trgCol = Math.min(Math.max(0, trgCol), targetGrid.getWidth() - setSize);
    // check if it is okay to move these stones
    if (!isTargetSafe(srcCol, srcRow, trgCol, trgRow,
        sourceGrid == targetGrid, targetGrid.getStones(), setSize)) {
      return false;
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
    trace.push(new MoveTrace(Move.TABLE_MOVE, sourcePosition, targetPosition));
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
    trace.push(new MoveTrace(Move.PUT_STONE, sourcePosition, targetPosition));
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
  @Override public boolean moveSetOnHand(int playerId, Coordinate sourcePosition,
      Coordinate targetPosition) {
    Grid hand = players.get(playerId).getHand();
    return moveSet(sourcePosition, targetPosition, hand, hand,
        (srcPos, trgPos) -> moveStoneOnHand(playerId, srcPos, trgPos));
  }

  /**
   * Moves stone from the given sourcePosition to the given targetPosition
   * on the players Hand with the ID of the given playerId.
   * If the player with the given playerId is the current player ID,
   * this move will be stored in the moveData for reset.
   *
   * @param playerId       the ID of the player who moves the stone on their hand
   * @param sourcePosition the position of the subject stone before moving
   * @param targetPosition the position of the subject stone after moving
   */
  @Override
  public void moveStoneOnHand(int playerId, Coordinate sourcePosition, Coordinate targetPosition) {
    players.get(playerId).moveStone(sourcePosition, targetPosition);
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
   * Removes the player with the given playerId
   * out of this game and reset their stones into the bag.
   *
   * @param playerId the ID of the player who left
   */
  @Override public void removePlayer(int playerId) {
    System.out.println("---number of players: " + players.size());
    if (!gameOn) {
      players.remove(playerId);
      return;
    }
    // remove the player with the playerId and reset their hand into the bag
    bag.addStones(players.remove(playerId).getStones().values());
    if (players.size() < MIN_PLAYERS) {
      gameOn = false;
      return;
    }
    if (currentPlayerId == playerId) {
      nextTurn();
    }
  }

  /** Resets all moves of the current player on this table and from their hand. */
  @Override public void reset() {
    while (!trace.empty()) {
      undo();
    }
  }

  /** Undoes the last move of the current player. */
  @Override public void undo() {
    if (trace.empty()) {
      return;
    }
    MoveTrace lastMove = trace.pop();
    Coordinate sourcePosition = lastMove.getSourcePosition();
    Coordinate targetPosition = lastMove.getTargetPosition();
    switch (lastMove.getMove()) {
      case TABLE_MOVE:
        // swap back stones on the table
        swapStoneOnTable(targetPosition, sourcePosition);
        return;
      case PUT_STONE:
      default:
        // get back stone from the table to the player hand
        Stone stone = table.removeStone(targetPosition);
        currentPoints -= stone.getNumber();
        currentPlayer().pushStone(stone);
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
   * Checks the consistency of all of moves the current Player did.
   * This Game is consistent, if the current Player has played at least a Stone from their Hand
   * and played total 30 points of stones from Hand if it was their first turn
   * and the Table is consistent.
   *
   * @return true if only if this Game is consistent
   */
  @Override public boolean isConsistent() {
    // check if the current player has played at least a Stone
    if (currentPoints == 0) {
      return false;
    }
    // check if the current player has played their (first) turn in this game
    if (/*!currentPlayer().hasPlayedFirstMove() && currentPoints < MIN_FIRST_MOVE_POINTS || */
        // and the consistency of the Table
        !table.isConsistent()) {
      return false;
    }
    currentPlayer().notifyEndOfFirstMove();
    // clear the moveData for the next turn
    trace.clear();
    // check if this player has won
    if (hasWinner()) {
      gameOn = false;
    }
    nextTurn();
    return true;
  }

  @Override public Map<Coordinate, Stone> getTableStones() {
    return table.getStones();
  }

  @Override public Map<Coordinate, Stone> getPlayerStones(int playerId) {
    return players.get(playerId).getStones();
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
   * sorts stones on the hand of the player with the given playerId by group-set.
   *
   * @param playerId the id of the player whose hand will be sorted
   */
  @Override public void sortPlayerHandByGroup(int playerId) {
    players.get(playerId).sortHandByGroup();
  }

  /**
   * sorts stones on the hand of the player with the given playerId by run-set.
   *
   * @param playerId the id of the player whose hand will be sorted
   */
  @Override public void sortPlayerHandByRun(int playerId) {
    players.get(playerId).sortHandByRun();
  }

  @Override public boolean hasPlayerPlayedFirstMove(int playerId) {
    return players.get(playerId).hasPlayedFirstMove();
  }

  @Override public boolean isGameOn() {
    return gameOn;
  }

  @Override public int getCurrentPlayerId() {
    return currentPlayerId;
  }

  @Override public int getTableWidth() {
    return table.getWidth();
  }

  @Override public int getTableHeight() {
    return table.getHeight();
  }

  @Override public int getPlayerHandWidth(int playerId) {
    return players.get(playerId).getHandWidth();
  }

  @Override public int getPlayerHandHeight(int playerId) {
    return players.get(playerId).getHandHeight();
  }

  @Override public int getNumberOfPlayers() {
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

  Stack<MoveTrace> getTrace() {
    return trace;
  }

  /**
   * Draws a Stone when if there are Stones available.
   * Switches to the next player if not.
   */
  @Override
  public void timeOut() {
    if (this.getBagSize() > 0) {
      this.drawStone();
    } else {
      this.nextTurn();
    }
  }
}

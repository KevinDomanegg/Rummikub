package game;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.stream.Collectors;

/** Model for the board game Rummikub. */
public class RummiGame implements Game {
  private static final  int MIN_PLAYERS = 2;
  private static final int MAX_PLAYERS = 4;
  // the number of stones players receive at the beginning
  private static final int FIRST_STONES = 14;
  // the minimal points every player should make for their first move
  private static final int MIN_FIRST_MOVE_POINTS = 30;

  private RummiTable table; // table of the game
//  private ArrayList<Player> players; // list of players
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

  /** gives a current player. */
  private Player currentPlayer() {
    return players.get(currentPlayerID);
  }

  /** shifts the currentPlayerID depending on the number of players in this game. */
  private void nextTurn() {
    // reset currentPoints
    currentPoints = 0;
    // the ID of the current player will be updated
    do {
      currentPlayerID = (currentPlayerID + 1) % MAX_PLAYERS;
    } while (!players.containsKey(currentPlayerID));
  }

  /** adds a new player with name and age in this game before the game start. */
  @Override public void setPlayer(int playerID, String name, int age) {
    players.put(playerID, new Player(name, age));
  }

  /** starts the game by handing out stones and determining the start player. */
  @Override public void start() {
    if (players.size() >= MIN_PLAYERS) {
      bag = new RummiBag();
      table.clear();
      handOutStones();
      setStartPlayer();
      isGameOn = true;
    }
    // ?? error?
    // throw new IllegalStateException("not enough players to start the game.");
  }

  /** first stones will be handed out randomly to each player. */
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

  /** the youngest player will be the first to play. */
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
   * moves stone from the given sourcePosition to the given targetPosition on the table.
   * If a stone at the targetPosition already exist, it will be swapped.
   * Hereby, this move will be stored in the trace for reset.
   *
   * @param sourcePosition the position of the subject stone before moving
   * @param targetPosition the position of the subject stone after moving
   */
  @Override public void moveStoneOnTable(Coordinate sourcePosition, Coordinate targetPosition){
    swapStoneOnTable(sourcePosition, targetPosition);
    // store this move.
    trace.push(new MoveTrace("MOVESTONEONTABLE", sourcePosition, targetPosition));
  }

  /**
   * moves the stone at the given sourcePosition to the given targetPosition with its
   * neighbor stones. If there are other (not neighbor) stones around the given targetPosition,
   * table stays unchanged. Hereby, the positions of the subject stone and its neighbors
   * after being moved will be automatically suit on the table if their positions are acceptable.
   *
   * @param sourcePosition the position of the subject stone before moving it
   * @param targetPosition the wanted position of the subject stone after moving it
   */
  @Override public void moveSetOnTable(Coordinate sourcePosition, Coordinate targetPosition) {
    Map<Coordinate, Stone > tableStones = table.getStones();
    // check if there is a stone on the table at the given sourcePosition
    if (!tableStones.containsKey(sourcePosition)) {
      return;
    }
    Coordinate coordinate = table.getFirstCoordOfSetAt(sourcePosition);
    int srcCol = coordinate.getCol();
    int srcRow = coordinate.getRow();
    int trgCol = targetPosition.getCol() - (sourcePosition.getCol() - srcCol);
    int trgRow = targetPosition.getRow();
    HashSet<Coordinate> selectedCoordinates = new HashSet<>(); // coordinates of stones to be moved
    for (int i = 0; tableStones.containsKey(coordinate); i++) {
      selectedCoordinates.add(coordinate);
      coordinate = new Coordinate(srcCol + i, srcRow);
    }
    int setSize = selectedCoordinates.size();
    // make trgCol suitable on the table
    trgCol = Math.min(Math.max(0, trgCol), table.getWidth() - setSize - 1);
    // check if it is safe to move stones
    for (int i = 0; i < setSize; i++) {
      coordinate = new Coordinate(trgCol + i, trgRow);
      // check if coordinate is clear or is a part of selectedCoordinates of moving stones
      if (tableStones.containsKey(coordinate) && !selectedCoordinates.contains(coordinate)) {
        return;
      }
    }
    // check if targetPosition was at the right side of the sourcePosition
    if (trgCol > srcCol) {
      // move stones starting from right
      for (int i = setSize - 1; i >= 0; i--) {
        moveStoneOnTable(new Coordinate(srcCol + i, srcRow), new Coordinate(trgCol + i, trgRow));
      }
    } else {
      // move stones starting from left
      for (int i = 0; i < setSize; i++) {
        moveStoneOnTable(new Coordinate(srcCol + i, srcRow), new Coordinate(trgCol + i, trgRow));
      }
    }
  }

  /**
   * swaps stones between the given sourcePosition to the given targetPosition on the table.
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
  }

  /**
   * puts a stone from the current player hand to the table if the target position is empty.
   * Hereby, this move will be stored in the trace for reset.
   *
   * @param sourcePosition the position of the subject stone before putting
   * @param targetPosition the position of the subject stone after putting
   */
  @Override public void putStone(Coordinate sourcePosition, Coordinate targetPosition) {
    // check if target position is empty
    if (!table.getStones().containsKey(targetPosition)) {
      Stone movingStone = currentPlayer().popStone(sourcePosition);
      if (movingStone == null) {
        return;
      }
      // add up the currentPoints
      currentPoints += movingStone.getNumber();
      table.setStone(targetPosition, movingStone);
      // store this move
      trace.push(new MoveTrace("MOVESTONEFROMHAND", sourcePosition, targetPosition));
    }
  }

  /**
   * moves stone from the given sourcePosition to the given targetPosition
   * on the players Hand with the ID of the given playerID.
   * If the player with the given playerID is the current player ID,
   * this move will be stored in the trace for reset.
   *
   * @param playerID the ID of the player who moves the stone on their hand
   * @param sourcePosition the position of the subject stone before moving
   * @param targetPosition the position of the subject stone after moving
   */
  @Override public void moveStoneOnHand(int playerID, Coordinate sourcePosition, Coordinate targetPosition) {
    players.get(playerID).moveStone(sourcePosition, targetPosition);
//    if (playerID == currentPlayerID){
//      // store this move
//      trace.push(new MoveTrace("MOVESTONEONHAND", sourcePosition, targetPosition));
//    }
  }

  /** makes the current player draw a stone from the bag and finish their turn. */
  @Override public void drawStone(){
    currentPlayer().pushStone(bag.removeStone());
    nextTurn();
  }


  /**
   * kicks the player with the given playerID out of this game and reset their stones into the bag.
   *
   * @param playerID the ID of the player who left
   */
  @Override public void playerHasLeft(int playerID) {
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

  /** resets all moves of the current player. */
  @Override public void reset(){
    while (!trace.empty()) {
      undo();
    }
//    currentPoints = 0;
  }

  /** undoes the last move of the current player. */
  @Override public void undo() {
    if (trace.empty()){
      return;
    }

    MoveTrace lastCommand = trace.pop();
    Coordinate sourcePosition = lastCommand.getInitialPosition();
    Coordinate targetPosition = lastCommand.getTargetPosition();
    String command = lastCommand.getCommand();
//    int playerID = lastCommand.getPlayerID();

    switch (command) {
      case "MOVESTONEONTABLE":
        // swap back stones on the table
        swapStoneOnTable(targetPosition, sourcePosition);
//        table.setStone(sourcePosition, table.removeStone(targetPosition));
        return;
      case "MOVESTONEFROMHAND":
        // get back stone from the table to the player hand
        Stone stone = table.removeStone(targetPosition);
        currentPoints -= stone.getNumber();
        currentPlayer().pushStone(stone);
        return;
//      case "MOVESTONEONHAND":
//        // swap back stones on the player hand
//        currentPlayer().moveStone(targetPosition, sourcePosition);
////        players.get(playerID).moveStone(targetPosition, sourcePosition);
//        return;
      default:
        //error Message: There are no moves to undo.
    }
  }

  /**
   * checks if the current player has no stone on hand and gives the result.
   *
   * @return true if only if the current player has won this game
   */
  @Override public boolean hasWinner() {
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
  @Override public boolean isConsistent() {
    // check if the current player has played something yet
    if (currentPoints == 0) {
      return false;
    }
    // check if the current player has played their (first) turn in this game
    if (/*!currentPlayer().hasPlayedFirstMove() && currentPoints < MIN_FIRST_MOVE_POINTS || */!table.isConsistent()) {
      currentPoints = 0;
      reset();
      return false;
    }
    // clear the trace for the next turn
    trace.clear();
    currentPlayer().playedFirstMove();
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

  public static void main(String[] args) {
    RummiGame game = new RummiGame();
    game.setPlayer(0, "player1", 0);
    game.setPlayer(1, "player2", 3);
    game.start();

    game.putStone(new Coordinate(4, 0), new Coordinate(3, 2));
    game.putStone(new Coordinate(5, 0), new Coordinate(4, 2));
    game.putStone(new Coordinate(6, 0), new Coordinate(5, 2));
    game.putStone(new Coordinate(7, 0), new Coordinate(6, 2));

    game.putStone(new Coordinate(0, 0), new Coordinate(3, 3));
    game.putStone(new Coordinate(1, 0), new Coordinate(4, 3));
    game.putStone(new Coordinate(2, 0), new Coordinate(5, 3));
    game.putStone(new Coordinate(3, 0), new Coordinate(6, 3));

    game.trace.clear();
    String firstTable = game.table.toString();
    System.out.println("---first table");
    System.out.println(firstTable);
    game.moveSetOnTable(new Coordinate(6, 2), new Coordinate(0, 0));
    System.out.println("---moving set (6, 2) to (0, 0)");
    System.out.println(game.table);
    game.moveSetOnTable(new Coordinate(3, 0), new Coordinate(10, 3));
    System.out.println("---moving set (3, 0) to (10, 3)");
    System.out.println(game.table);
    game.moveSetOnTable(new Coordinate(8, 3), new Coordinate(4, 3));
    System.out.println("---moving set (8, 3) to (4, 3)");
    System.out.println(game.table);
    game.moveStoneOnTable(new Coordinate(5, 3), new Coordinate(0, 0));
    System.out.println("---moving stone (5, 3) to (0, 0)");
    System.out.println(game.table);
    game.moveSetOnTable(new Coordinate(3, 3), new Coordinate(20, 1));
    System.out.println("---moving set (3, 3) to (20, 1)");
    System.out.println(game.table);

    game.reset();
    System.out.println("---reset");
    System.out.println(game.table);
    System.out.println("---check if reset worked");
    System.out.println(game.table.toString().equals(firstTable));
  }
}

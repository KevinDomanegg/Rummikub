package game;


import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
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

  private RummiTable table;
  private ArrayList<Player> players;
  private RummiBag bag;
  private Stack<MoveTrace> trace;
  private int currentPlayerID;
  private int firstMovePoints; // the points of the first move of a current player

  public RummiGame() {
    table = new RummiTable();
    players = new ArrayList<>(MAX_PLAYERS);
    bag = new RummiBag();
    trace = new Stack<>();
  }

  /** gives a current player. */
  private Player currentPlayer() {
    return players.get(currentPlayerID);
  }

  private void nextTurn() {
    // the ID of the current player will be updated
    currentPlayerID = (currentPlayerID + 1) % players.size();
  }

  @Override public void setPlayer(String name, int age) {
    players.add(new Player(name, age));
  }

  /** starts the game by handing out stones and determining the start player. */
  @Override public void start() {
    if (players.size() >= MIN_PLAYERS) {
      handOutStones();
      setStartPlayer();
    }
    // ?? error?
    // throw new IllegalStateException("not enough players to start the game.");
  }

  private void handOutStones() {
    for (int i = 0; i < FIRST_STONES; i++) {
      for (int j = 0; j < players.size(); j++) {
        drawStone();
      }
    }
  }

  private void setStartPlayer() {
    // currentPlayerID is with 0 automatically initialized
    for (int playerID = 1; playerID < players.size(); playerID++) {
      Player player = players.get(playerID);
      if (player.getAge() < currentPlayer().getAge()) {
        currentPlayerID = playerID;
      }
    }
  }

  @Override public void moveStoneOnTable(Coordinate initialPosition, Coordinate targetPosition){
    table.setStone(targetPosition, table.getStones().remove(initialPosition));
    trace.push(new MoveTrace("MOVESTONEONTABLE", initialPosition, targetPosition));
  }

  @Override public void putStone(Coordinate initialPosition, Coordinate targetPosition){
    Stone movingStone = currentPlayer().popStone(initialPosition);
    // add up the firstMovePoints if the current player hasn't played their first move yet
//    if (!currentPlayer().hasPlayedFirstMove()) {
//      firstMovePoints += movingStone.getNumber();
//    }
    table.setStone(targetPosition, movingStone);
    trace.push(new MoveTrace("MOVESTONEFROMHAND", initialPosition, targetPosition));
  }

  @Override
  public void moveStoneOnHand(int playerID, Coordinate initialPosition, Coordinate targetPosition) {
    players.get(playerID).moveStone(initialPosition, targetPosition);
    if (playerID == currentPlayerID){
      trace.push(new MoveTrace("MOVESTONEONHAND", initialPosition, targetPosition));
    }
  }

  @Override public void drawStone(){
    currentPlayer().pushStone(bag.removeStone());
    nextTurn();
  }


  @Override public void playerHasLeft(int playerID) {
    bag.addStones(players.get(playerID).getStones().values());
    nextTurn();
  }

  @Override
  public void reset(){
    while (!trace.empty()) {
      undo();
    }
  }

  @Override
  public void undo() {
    if (trace.empty()){
      return;
    }

    MoveTrace lastCommand = trace.pop();
    Coordinate initialPosition = lastCommand.getInitialPosition();
    Coordinate targetPosition = lastCommand.getTargetPosition();
    String command = lastCommand.getCommand();
    int playerID = lastCommand.getPlayerID();

    switch (command) {
      case "MOVESTONEONTABLE":
        table.setStone(initialPosition, table.getStones().remove(targetPosition));
        break;
      case "MOVESTONEFROMHAND":
        Stone stone = table.getStones().remove(targetPosition);
        currentPlayer().getStones().put(initialPosition, stone);
        break;
      case "MOVESTONEONHAND":
        players.get(playerID).moveStone(targetPosition, initialPosition);
        break;
      default:
        //error Message: There are no moves to undo.
        break;
    }
  }

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
    if (!currentPlayer().hasPlayedFirstMove() && firstMovePoints < MIN_FIRST_MOVE_POINTS) {
      firstMovePoints = 0;
      reset();
      return false;
    }
    if (table.isConsistent()) {
      // clear the trace for the next turn
      trace.clear();
      currentPlayer().hasPlayedFirstMove();
      nextTurn();
      return true;
    }
    reset();
    return false;
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
    return players.stream().map(Player::getHandSize).collect(Collectors.toList());
  }

  @Override public List<String> getPlayerNames() {
    return players.stream().map(Player::getName).collect(Collectors.toList());
  }

  @Override public int getCurrentPlayerID(){
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
  @Override public List<Entry<Integer, Integer>> getFinalRank() {
    List<Entry<Integer, Integer>> rank = new ArrayList<>(players.size());
    for (int playerID = 0; playerID < players.size(); playerID++) {
      rank.add(new SimpleEntry<>(playerID, players.get(playerID).getPoints()));
    }
    rank.sort((x, y) -> x.getKey().compareTo(y.getValue()));
    return rank;
  }
}

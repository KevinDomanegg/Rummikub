package game;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;


public class RummiGame implements Game {
  private static final int MAX_PLAYERS = 4;
  private static final int FIRST_STONES = 14;

  private RummiTable table;
  private ArrayList<Player> players;
  private RummiBag bag;
  private Stack<MoveTrace> trace;
  private int currentPlayerID;

  public RummiGame() {
    table = new RummiTable();
    players = new ArrayList<>(MAX_PLAYERS);
    bag = new RummiBag();
    trace = new Stack<>();
  }

  private Player currentPlayer() {
    return players.get(currentPlayerID);
  }

  private void nextTurn() {
    currentPlayerID = (currentPlayerID + 1) % players.size();
  }

  @Override public void setPlayer(int age) {
    players.add(new Player(age));
  }

  @Override public void start() {
    handOutStones();
    setStarter();
  }

  private void handOutStones() {
    for (int i = 0; i < FIRST_STONES; i++) {
      for (int j = 0; j < players.size(); j++) {
        drawStone();
      }
    }
  }

  private void setStarter() {
    currentPlayerID = 0;
    for (int i = 1; i < players.size(); i++) {
      Player player = players.get(i);
      if (player.getAge() < currentPlayer().getAge()) {
        currentPlayerID = i;
      }
    }
  }

  @Override
  public void moveStoneOnTable(Coordinate initialPosition, Coordinate targetPosition){
    table.setStone(targetPosition, table.getStones().remove(initialPosition));
    trace.push(new MoveTrace("MOVESTONEONTABLE", initialPosition, targetPosition));
  }

  @Override
  public void moveStoneFromHand(Coordinate initialPosition, Coordinate targetPosition){
    table.setStone(targetPosition, currentPlayer().popStone(initialPosition));
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
    while (trace.empty() == false) {
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

  @Override public boolean isConsistent() {
    boolean isTableConsistent = table.isConsistent();
    if (isTableConsistent) {
      nextTurn();
    }
    return isTableConsistent;
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
    // need to be discussed, how does sever know in what sequence the player's hand sizes are stored
    return players.stream().map(Player::getHandSize).collect(Collectors.toList());
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

  public Stack<MoveTrace> getTrace() {
    return trace;
  }


}
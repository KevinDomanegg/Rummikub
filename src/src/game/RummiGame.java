package game;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RummiGame implements Game {
  private static final int MAX_PLAYERS = 4;
  private static final int FIRST_STONES = 14;

  private RummiTable table;
  private ArrayList<Player> players;
  private RummiBag bag;
  private int currentPlayerPosition;

  public RummiGame() {
    table = new RummiTable();
    players = new ArrayList<>(MAX_PLAYERS);
    bag = new RummiBag();
  }

  private Player currentPlayer() {
    return players.get(currentPlayerPosition);
  }

  private void nextTurn() {
    currentPlayerPosition = (currentPlayerPosition + 1) % players.size();
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
    currentPlayerPosition = 0;
    for (int i = 1; i < players.size(); i++) {
      Player player = players.get(i);
      if (player.getAge() < currentPlayer().getAge()) {
        currentPlayerPosition = i;
      }
    }
  }

  @Override
  public void moveStoneOnTable(Coordinate initialPosition, Coordinate targetPosition){
    table.setStone(targetPosition, table.getStones().remove(initialPosition));
  }

  @Override
  public void moveStoneFromHand(Coordinate initialPosition, Coordinate targetPosition){
    table.setStone(targetPosition, currentPlayer().popStone(initialPosition));

//    Stone movedStone = players.get(players.get(currentPlayerPosition)).getStones().get(initialPosition);
//
//    table.setStone(targetPosition, movedStone);
//    currentHand.getStones().remove(initialPosition);
  }

  @Override
  public void moveStoneOnHand(int playerPosition, Coordinate initialPosition, Coordinate targetPosition) {
    players.get(playerPosition).moveStone(initialPosition, targetPosition);
  }

  @Override public void drawStone(){
    currentPlayer().pushStone(bag.removeStone());
    nextTurn();
  }


  @Override public void playerHasLeft(int playerPosition) {
    bag.addStones(players.get(playerPosition).getStones().values());
    nextTurn();
  }

  @Override
  public void undo() {

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

  @Override public Map<Coordinate, Stone> getCurrentPlayerStones() {
    return currentPlayer().getStones();
  }

  @Override public int getBagSize() {
    return bag.size();
  }

  @Override public List<Integer> getPlayerHandSizes() {
    // need to be discussed, how does sever know in what sequence the player's hand sizes are stored
    return players.stream().map(Player::getHandSize).collect(Collectors.toList());
  }

  @Override public int getCurrentPlayerPosition(){
    return currentPlayerPosition;
  }

  @Override public int getTableWidth() {
    return table.getWidth();
  }

  @Override public int getTableHeight() {
    return table.getHeight();
  }

  @Override public int getCurrentPlayerHandWidth() {
    return currentPlayer().getHandWidth();
  }

  @Override public int getCurrentPlayerHandHeight() {
    return currentPlayer().getHandHeight();
  }
}

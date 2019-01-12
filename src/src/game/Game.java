package game;

import java.util.List;
import java.util.Map;

public interface Game {

  void setPlayer(int age);

  void start();

  void moveStoneOnTable(Coordinate initialPosition, Coordinate targetPosition);

  void moveStoneFromHand(Coordinate initialPosition, Coordinate targetPosition);

  void moveStoneOnHand(int playerPosition, Coordinate initialPosition, Coordinate targetPosition);

  void drawStone();

  void playerHasLeft(int playerPosition);

  void undo();

  boolean hasWinner();

  boolean isConsistent();

  Map<Coordinate, Stone> getTableStones();

  Map<Coordinate, Stone> getCurrentPlayerStones();

  List<Integer> getPlayerHandSizes();

  int getBagSize();

  int getCurrentPlayerPosition();








  // ? int getTime();
}

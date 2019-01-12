package game;

import java.util.List;
import java.util.Map;

public interface Game {

  void moveStoneOnTable(Coordinate currentCoord, Coordinate nextCoord);

  void moveStoneFromHand(Coordinate currentCoord, Coordinate nextCoord);

  void moveStoneOnHand(Coordinate currentCoord, Coordinate nextCoord);

  void drawStone();

  void playerHasLeft();

  void undo();

  void nextTurn();

  boolean hasWinner();

  boolean isConsistent();

  Map<Coordinate, Stone> getTableStones();

  Map<Coordinate, Stone> getCurrentHandStones();

  List<Integer> getOtherHandSizes();

  int getBagSize();

  int getCurrentHandNumber();








  // ? int getTime();
}

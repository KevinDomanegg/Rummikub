package game;

import java.util.List;
import java.util.Map;

public interface Game {

  void start();

  void moveStoneonTable(Coordinate currentCoord, Coordinate nextCoord);

  void moveStoneFromHand(Coordinate currentCoord, Coordinate nextCoord);

  void moveStoneOnHand(Coordinate currentCoord, Coordinate nextCoord);

  void reset(int moves);

  void undo();

  boolean isConsistent();

  void drawStone();

  void handDown();

  boolean hasWinnder();

  Map<Coordinate, Stone> getTable();

  Map<Coordinate, Stone> getCurrentHand();

  List<Integer> getOtherHandSizes();

  int getBagSize();

  // ? int getTime();
}

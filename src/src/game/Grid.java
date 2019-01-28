package game;

import java.util.Map;

public interface Grid {

  Map<Coordinate, Stone> getStones();

  void setStone(Coordinate coordinate, Stone stone);

  Stone removeStone(Coordinate coordinate);

  int getWidth();

  int getHeight();

  void clear();

  default Coordinate getFirstCoordOfSetAt(Coordinate coordinate) {
    int col = coordinate.getCol();
    // find the first stone for a potential set
    while (getStones().containsKey(new Coordinate(col - 1, coordinate.getRow()))) {
      col--;
    }
    return new Coordinate(col, coordinate.getRow());
  }
}


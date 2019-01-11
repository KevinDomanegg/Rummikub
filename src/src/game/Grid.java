package game;

import java.util.Map;

public interface Grid {

  Map<Coordinate, Stone> getStnes();

  void setStone(Coordinate coordinate, Stone stone);

  int getWidth();

  int getHeight();

  void clear();
}


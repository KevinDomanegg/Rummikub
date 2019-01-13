package communication.gameinfo;

import game.Coordinate;
import game.Stone;
import java.util.Map;

abstract class GridInfo {
  private Map<Coordinate, Stone> stones;

  GridInfo(int width, int height, Map<Coordinate, Stone> stones) {
    this.stones = stones;
  }

  public Map<Coordinate, Stone> getStones() {
    return stones;
  }
}

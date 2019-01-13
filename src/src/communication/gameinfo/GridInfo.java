package communication.gameinfo;

import game.Coordinate;
import game.Stone;
import java.util.Map;

abstract class GridInfo {
  private Map<Coordinate, Stone> stones;
  private int width;
  private int height;

  GridInfo(int width, int height, Map<Coordinate, Stone> stones) {
    this.stones = stones;
    this.width = width;
    this.height = height;
  }

  public Map<Coordinate, Stone> getStones() {
    return stones;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

}

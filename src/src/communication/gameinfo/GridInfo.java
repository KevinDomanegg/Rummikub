package communication.gameinfo;

import java.io.Serializable;

public abstract class GridInfo implements Serializable {
  StoneInfo[][] grid;

  GridInfo(StoneInfo[][] grid) {
    this.grid = grid;
  }

  public StoneInfo[][] getGrid() {
    return grid;
  }

}

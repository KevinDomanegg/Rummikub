package communication.gameinfo;

import java.io.Serializable;

/**
 * Info for Game Table or Player Hand.
 */
public class GridInfo implements GameInfo, Serializable {
  private StoneInfo[][] grid;
  private GameInfoID gameInfoID;

  public GridInfo(GameInfoID gameInfoID, StoneInfo[][] grid) {
    this.gameInfoID = gameInfoID;
    this.grid = grid;
  }

  public StoneInfo[][] getGrid() {
    return grid;
  }

  @Override public GameInfoID getGameInfoID() {
    return gameInfoID;
  }
}

package communication.gameinfo;

import game.Coordinate;
import game.Stone;
import java.io.Serializable;
import java.util.Map;

public class TableInfo extends GridInfo implements GameInfo, Serializable {

  public TableInfo(int width, int height, Map<Coordinate, Stone> stones) {
    super(width, height, stones);
  }

  @Override
  public InfoID getInfoID() {
    return InfoID.TABLE;
  }
}

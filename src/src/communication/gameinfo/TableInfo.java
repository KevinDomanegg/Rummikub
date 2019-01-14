package communication.gameinfo;

import game.Coordinate;
import game.Stone;
import java.util.Map;

public class TableInfo extends GridInfo implements GameInfo {

  public TableInfo(int width, int height, Map<Coordinate, Stone> stones) {
    super(width, height, stones);
  }

  @Override
  public InfoID getInfoID() {
    return InfoID.TABLE;
  }
}

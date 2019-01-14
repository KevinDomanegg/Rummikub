package communication.gameinfo;

import game.Coordinate;
import game.Stone;
import java.util.Map;

public class HandInfo extends GridInfo implements GameInfo {

  public HandInfo(int width, int height, Map<Coordinate, Stone> stones) {
    super(width, height, stones);
  }

  @Override
  public InfoID getInfoID() {
    return InfoID.HAND;
  }
}

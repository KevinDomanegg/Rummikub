package communication.gameinfo;

import java.io.Serializable;
import java.util.List;

public class HandSizesInfo implements GameInfo, Serializable {
  private List<Integer> otherHandSizes;

  public HandSizesInfo(List<Integer> otherHandSizes) {
    this.otherHandSizes = otherHandSizes;
  }

  @Override public GameInfoID getGameInfoID() {
    return GameInfoID.HAND_SIZES;
  }

  public List<Integer> getHandSizes() {
    return otherHandSizes;
  }
}

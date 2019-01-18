package communication.gameinfo;

import java.io.Serializable;

public class BagInfo implements GameInfo, Serializable {
  private int size;

  public BagInfo(int size) {
    this.size = size;
  }

  @Override
  public GameInfoID getGameInfoID() {
    return GameInfoID.BAG;
  }

  public int getSize() {
    return size;
  }
}

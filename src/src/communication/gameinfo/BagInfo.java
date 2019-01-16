package communication.gameinfo;

import java.io.Serializable;

public class BagInfo implements GameInfo, Serializable {
  private int size;

  public BagInfo(int size) {
    this.size = size;
  }

  @Override
  public InfoID getInfoID() {
    return InfoID.BAG;
  }

  public int getSize() {
    return size;
  }
}

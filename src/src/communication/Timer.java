package communication;

import java.io.Serializable;

public class Timer implements GameInfo, Serializable {

  private InfoID id;

  public Timer(InfoID id) {
    this.id = id;
  }

  @Override
  public InfoID getInfoID() {
    return InfoID.DRAW;
  }
}

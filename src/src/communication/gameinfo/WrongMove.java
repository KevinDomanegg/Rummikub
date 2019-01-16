package communication.gameinfo;

import java.io.Serializable;

public class WrongMove implements GameInfo, Serializable {

  @Override
  public InfoID getInfoID() {
    return InfoID.WRONG_MOVE;
  }
}

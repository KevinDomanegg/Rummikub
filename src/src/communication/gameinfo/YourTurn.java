package communication.gameinfo;

import java.io.Serializable;

/** game info to be sent when it's a players turn. */
public class YourTurn implements GameInfo, Serializable {

  @Override
  public InfoID getInfoID() {
    return InfoID.YOUR_TURN;
  }
}

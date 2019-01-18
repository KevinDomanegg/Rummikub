package communication.gameinfo;

import java.io.Serializable;

public class CurrentPlayerInfo implements GameInfo, Serializable {
  private int playerID;

  public CurrentPlayerInfo(int playerID) {
    this.playerID = playerID;
  }

  @Override public GameInfoID getGameInfoID() {
    return GameInfoID.CURRENT_PLAYER;
  }

  public int getPlayerID() {
    return playerID;
  }
}

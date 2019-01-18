package communication.gameinfo;

import java.io.Serializable;

/**
 * Info that does not store any but InfoId.
 */
public class SimpleGameInfo implements GameInfo, Serializable {
  private GameInfoID gameInfoID;

  public SimpleGameInfo(GameInfoID gameInfoID) {
    this.gameInfoID = gameInfoID;
  }

  @Override public GameInfoID getGameInfoID() {
    return gameInfoID;
  }

}

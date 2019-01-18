package communication.gameinfo;

import java.io.Serializable;

/** Game Info that represents someone in the game drew a stone.*/
public class DrawInfo implements GameInfo, Serializable {

  @Override public GameInfoID getGameInfoID() {
    return GameInfoID.DRAW;
  }
}

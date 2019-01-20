package communication.gameinfo;

import java.io.Serializable;

/**
 * name info for a new player who just joined the game.
 */
public class NameInfo implements GameInfo, Serializable {

  private String username;

  public NameInfo(String username) {
    this.username = username;
  }

  public String getUsername() {
    return this.username;
  }

  @Override
  public GameInfoID getGameInfoID() {
    return GameInfoID.USERNAME;
  }
}

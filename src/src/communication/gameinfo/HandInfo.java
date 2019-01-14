package communication.gameinfo;

/** game player hand model for view. */
public class HandInfo extends GridInfo implements GameInfo {

  public HandInfo(StoneInfo[][] grid) {
    super(grid);
  }

  @Override public InfoID getInfoID() {
    return InfoID.HAND;
  }
}

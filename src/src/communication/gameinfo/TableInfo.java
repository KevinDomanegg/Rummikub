package communication.gameinfo;

/** game table model for view. */
public class TableInfo extends GridInfo implements GameInfo {

  public TableInfo(StoneInfo[][] grid) {
    super(grid);
  }

  @Override
  public InfoID getInfoID() {
    return InfoID.TABLE;
  }
}

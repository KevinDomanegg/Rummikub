package communication.gameinfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map.Entry;

public class RankInfo implements GameInfo, Serializable {
  private final List<Entry<Integer, Integer>> finalRank;

  public RankInfo(List<Entry<Integer, Integer>> finalRank) {
    this.finalRank = finalRank;
  }

  @Override public GameInfoID getGameInfoID() {
    return GameInfoID.RANK;
  }

  public List<Entry<Integer, Integer>> getFinalRank() {
    return finalRank;
  }
}

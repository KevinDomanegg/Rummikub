package communication.gameinfo;

import java.io.Serializable;
import java.util.Map;

/**
 * GameInfo containing the Information about the final rank of the finished game.
 */
public class RankInfo implements GameInfo, Serializable {
  private final Map<Integer, Integer> finalRank;

  public RankInfo(Map<Integer, Integer> finalRank) {
    this.finalRank = finalRank;
  }

  @Override public GameInfoID getGameInfoID() {
    return GameInfoID.RANK;
  }

  public Map<Integer, Integer> getFinalRank() {
    return finalRank;
  }
}

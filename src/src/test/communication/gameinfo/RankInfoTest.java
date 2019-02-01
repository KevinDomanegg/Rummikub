package communication.gameinfo;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RankInfoTest {

  @Test
  public void initTest(){
    Map<Integer, Integer> finalRank = new HashMap<>();
    finalRank.put(1,1);

    RankInfo info = new RankInfo(finalRank);
    assertEquals(info.getFinalRank(), finalRank);
    assertEquals(info.getGameInfoID(), GameInfoID.RANK);
  }
}
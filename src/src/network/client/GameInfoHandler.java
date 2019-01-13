package network.client;

import communication.gameinfo.GameInfo;
import communication.gameinfo.HandInfo;
import communication.gameinfo.InfoID;
import communication.gameinfo.TableInfo;
import game.Coordinate;
import game.Stone;

import java.util.Map;

public class GameInfoHandler {

  private RummiClient client;

  public GameInfoHandler(RummiClient client) {
    this.client = client;
  }

  public Map<Coordinate, Stone> applyGameInfo(GameInfo gameinfo) {
    switch (gameinfo.getInfoID()) {
      case HAND:
        return ((HandInfo) gameinfo).getStones();
      case TABLE:
        return ((TableInfo) gameinfo).getStones();
      default: return null;
    }
  }


}

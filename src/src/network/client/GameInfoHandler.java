package network.client;

import communication.gameinfo.GameInfo;
import communication.gameinfo.HandInfo;
import communication.gameinfo.InfoID;
import communication.gameinfo.TableInfo;
import game.Coordinate;
import game.Stone;

import java.util.Map;

public class GameInfoHandler {

//  private RummiClient client;
  private Controller controller;

  public GameInfoHandler(Controller controller) {
    this.controller = controller;
//    this.client = client;
  }

  public void applyGameInfo(GameInfo gameinfo) {
    switch (gameinfo.getInfoID()) {
      case HAND:
        controller.setPlayerHand(((HandInfo) gameinfo).getGrid());
        return;
      case TABLE:
        controller.setTable(((TableInfo) gameinfo).getGrid());
        return;
      default:
    }
  }


}

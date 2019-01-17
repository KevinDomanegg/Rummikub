package network.client;

import communication.gameinfo.BagInfo;
import communication.gameinfo.GameInfo;
import communication.gameinfo.HandInfo;
import communication.gameinfo.HandSizesInfo;
import communication.gameinfo.TableInfo;

class GameInfoHandler {

//  private RummiClient client;
  private Controller controller;

  GameInfoHandler(Controller controller) {
    this.controller = controller;
//    this.client = client;
  }

  void applyGameInfo(GameInfo gameinfo) {
    switch (gameinfo.getInfoID()) {
      case HAND:
        controller.setPlayerHand(((HandInfo) gameinfo).getGrid());
        return;
      case TABLE:
        controller.setTable(((TableInfo) gameinfo).getGrid());
        return;
      case WRONG_MOVE:
        controller.printWrongMove();
        return;
      case BAG:
        controller.setBagSize(((BagInfo) gameinfo).getSize());
        return;
      case YOUR_TURN:
        controller.notifyTurn();
        return;
      case HAND_SIZES:
        controller.setHandSizes(((HandSizesInfo) gameinfo).getHandSizes());
        return;
      case DRAW:
        controller.countDownBagSize();
        return;
      default:
    }
  }


}

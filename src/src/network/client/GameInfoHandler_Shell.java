package network.client;

import communication.gameinfo.BagInfo;
import communication.gameinfo.GameInfo;
import communication.gameinfo.GridInfo;
import communication.gameinfo.HandSizesInfo;

class GameInfoHandler_Shell {

  private ShellController shellController;

  GameInfoHandler_Shell(ShellController shellController) {
    this.shellController = shellController;
//    this.client = client;
  }

  void applyGameInfo(GameInfo gameinfo) {
    switch (gameinfo.getGameInfoID()) {
      // case CURRENT_PLAYER:
      //
      case HAND:
        shellController.setPlayerHand(((GridInfo) gameinfo).getGrid());
        return;
      case TABLE:
        shellController.setTable(((GridInfo) gameinfo).getGrid());
        return;
      case INVALID_MOVE:
        shellController.printWrongMove();
        return;
      case BAG:
        shellController.setBagSize(((BagInfo) gameinfo).getSize());
        return;
      case YOUR_TURN:
        shellController.notifyTurn();
        return;
      case HAND_SIZES:
        shellController.setHandSizes(((HandSizesInfo) gameinfo).getHandSizes());
        return;
      default:
    }
  }


}

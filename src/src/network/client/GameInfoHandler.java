package network.client;

import communication.gameinfo.BagInfo;
import communication.gameinfo.CurrentPlayerInfo;
import communication.gameinfo.GameInfo;
import communication.gameinfo.GridInfo;
import communication.gameinfo.HandSizesInfo;
import communication.gameinfo.PlayerNamesInfo;
import view.NetworkController;

class GameInfoHandler {

  private NetworkController controller;

  GameInfoHandler(NetworkController controller) {
    this.controller = controller;
//    this.client = client;
  }

  void applyGameInfo(Object gameInfo) {
    switch (((GameInfo)gameInfo).getGameInfoID()) {
      // case CURRENT_PLAYER:
      case HAND:
        controller.setPlayerHand(((GridInfo) gameInfo).getGrid());
        return;
      case TABLE:
        controller.setTable(((GridInfo) gameInfo).getGrid());
        return;
      case INVALID_MOVE:
        controller.notifyInvalidMove();
        return;
      case BAG:
        controller.setBagSize(((BagInfo) gameInfo).getSize());
        return;
      case YOUR_TURN:
        controller.notifyTurn();
        return;
      case HAND_SIZES:
        controller.setHandSizes(((HandSizesInfo) gameInfo).getHandSizes());
        return;
      case PLAYER_NAMES:
        controller.setPlayerNames(((PlayerNamesInfo) gameInfo).getNames());
        return;
      case CURRENT_PLAYER:
        controller.notifyCurrentPlayer(((CurrentPlayerInfo) gameInfo).getPlayerID());
        return;
      case GAME_START:
        controller.notifyGameStart();
      default:
    }
  }


}

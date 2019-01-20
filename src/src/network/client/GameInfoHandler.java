package network.client;

import communication.gameinfo.*;
import view.NetworkController;

public class GameInfoHandler { //TODO FIGURE OUT A WAY TO BE NOT PUBLIC: PROBLEM: CLIENT-NETWORKCONTROLLER-GAMEINFOHANDLER

  private NetworkController controller;

  public GameInfoHandler(NetworkController controller) {
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
      case DRAW:
        break;
      case CURRENT_PLAYER:
        controller.notifyCurrentPlayer(((CurrentPlayerInfo) gameInfo).getPlayerID());
        return;
      case GAME_START:
        controller.notifyGameStart();
        return;
      case IP_ADDRESS:
        controller.setIPAddress(((GameIPAddress) gameInfo).getIpAddress());
        return;
      case USERNAME:
          controller.setUsername(((GameUsernames) gameInfo).getUsername(), ((GameUsernames) gameInfo).getId());
      default:
    }
  }


}

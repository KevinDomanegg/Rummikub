package network.client;

import communication.gameinfo.*;
import view.Controller;

/**
 * GameInfoHandler handles the received from the ClientListener gameinfos
 * and chooses where each should go.
 */
public class GameInfoHandler {

  private Controller controller;

  /**
   * Creates a new GameInfoHandler that contains a controller.
   *
   * @param controller an object of a class that implements the interface Controller
   */
  public GameInfoHandler(Controller controller) {
    this.controller = controller;
  }

  /**
   * Main method of the class: forwards the received information to the controller
   * accordingly it's GameInfoID.
   *
   * @param gameInfo the received Object that the Server sent.
   */
  void applyGameInfo(Object gameInfo) {
    switch (((GameInfo) gameInfo).getGameInfoID()) {
      // case CURRENT_PLAYER:
      case HAND:
        //System.out.println("handling hand");
        controller.setPlayerHand(((GridInfo) gameInfo).getGrid());
        return;
      case TABLE:
        //System.out.println("handling table");
        controller.setTable(((GridInfo) gameInfo).getGrid());
        return;
      case ERROR:
        controller.showError(((ErrorInfo) gameInfo).getErrorMessage());
        return;
      case BAG:
        //System.out.println("handling bag");
        controller.setBagSize(((BagInfo) gameInfo).getSize());
        return;
      case YOUR_TURN:
        System.out.println("handling yourturn");
        controller.notifyTurn();
        //controller.notifyCurrentPlayer(0);
        return;
      case HAND_SIZES:
        //System.out.println("handling handsizes");
        controller.setHandSizes(((HandSizesInfo) gameInfo).getHandSizes());
        return;
      case PLAYER_NAMES:
        //System.out.println("handling names");
        controller.setPlayerNames(((PlayerNamesInfo) gameInfo).getNames());
        return;
      case CURRENT_PLAYER:
        System.out.println("handling currentplayer");
        System.out.println("received " + ((CurrentPlayerInfo) gameInfo).getPlayerID() + " as current player");
        controller.notifyCurrentPlayer(((CurrentPlayerInfo) gameInfo).getPlayerID());
        return;
      case GAME_START:
        System.out.println("handling gamestart");
        controller.notifyGameStart();
        return;
      case RANK:
        controller.showRank(((RankInfo) gameInfo).getFinalRank());
        return;
      case TOO_MANY_CLIENTS:
        controller.connectionRejected();
        break;
      default:
    }
    System.out.println("Info handled");
  }

  /**
   * Notify the user that the Server is not available anymore.
   */
  void notifyServerClose() {
    controller.notifyServerClose();
  }
}

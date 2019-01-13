package network.server;
//Might be better to move this class to the game-package
import communication.request.ConcreteHandMove;
import communication.request.ConcretePutStone;
import communication.request.ConcreteSetPlayer;
import communication.request.ConcreteTableMove;
import communication.request.Request;
import communication.request.RequestID;
import game.Game;

public class RequestHandler {
  /**
   * Class that can take Request-Objects and transform the into method-calls for a Game.
   * Acts as a Link between RummiServer and Game.
   */


  private Game game;
  private Server server;

  public RequestHandler(Server server, Game game) {
    this.server = server;
    this.game = game;
  }


  public void applyRequest(Request request, int playerID){
    RequestID id = request.getRequestID();

    switch (id){
      case HAND_MOVE:
        ConcreteHandMove handMove = (ConcreteHandMove) request;
        game.moveStoneOnHand(playerID,
            handMove.getInitialCoordinate(), handMove.getTargetCoordinate());
        return;
      case TABLE_MOVE:
        ConcreteTableMove tableMove = (ConcreteTableMove) request;
        game.moveStoneOnTable(tableMove.getInitialCoordinate(), tableMove.getTargetCoordinate());
        return;
      case PUT_STONE:
        ConcretePutStone putStone = (ConcretePutStone) request;
        game.moveStoneFromHand(putStone.getInitialCoordinate(), putStone.getTargetCoordinate());
        return;
      case DRAW:
        game.drawStone();
        return;
      case CONFIRM_MOVE:
        if (!game.isConsistent()) {
          // send GameInfo that it was'nt good
        }
        return;
      case GIVE_UP:
        game.playerHasLeft(playerID);
        return;
      case SET_PLAYER:
        ConcreteSetPlayer setPlayer = (ConcreteSetPlayer) request;
        game.setPlayer(setPlayer.getAge());
        return;
      case START:
        game.start();
        return;
      case GET_HAND:
      case GET_TABLE:

      default:
    }
  }
}

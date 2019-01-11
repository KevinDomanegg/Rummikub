package communication;
//Might be better to move this class to the game-package
import communication.*;
import game.Game;

public class RequestHandler {
  /**
   * Class that can take Request-Objects and transform the into method-calls for a Game.
   * Acts as a Link between Server and Game.
   */

  private Game game;

  public RequestHandler(Game game) {
    this.game = game;
  }

  public void applyRequest(Request request){
    RequestID id = request.getRequestID();

    switch (id){
      case HAND_MOVE:
        Concrete_Hand_Move hand_move = (Concrete_Hand_Move) request;
        game.moveStoneOnHand(hand_move.getCurrentCoor(), hand_move.getNewCoor());
        break;

      case TABLE_MOVE:
        ConcreteTableMove tableMove = (ConcreteTableMove) request;
        game.moveStoneonTable(tableMove.getCurrentCoor(), tableMove.getNewCoor());
        break;

      case PUT_STONE:
        ConcretePutStone putStone = (ConcretePutStone) request;
        game.moveStoneFromHand(putStone.getCurrentCoor(), putStone.getNewCoor());
        break;

      case DRAW:
        game.drawStone();
        break;

      case CONFIRM_MOVE:

      case GIVE_UP:

      default:

    }
  }
}

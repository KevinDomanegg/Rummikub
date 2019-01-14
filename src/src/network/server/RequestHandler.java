package network.server;
//Might be better to move this class to the game-package
import communication.gameinfo.HandInfo;
import communication.gameinfo.TableInfo;
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
      case START:
        game.start();
        server.sendToAll(new TableInfo(game.getTableWidth(), game.getTableHeight(), game.getTableStones()));
        for (int i = 0; i < game.getNumberOfPlayers(); i++) {
          server.sendToPlayer(i, new HandInfo(game.getPlayerHandWidth(i),
              game.getPlayerHandHeight(i), game.getPlayerStones(i)));
        }
        return;
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
          // server.sendToPlayer(playerID, new WrongMove());
        } else {
          server.sendToAll(new TableInfo(game.getTableWidth(), game.getTableHeight(), game.getTableStones()));
        }
        return;
      case GIVE_UP:
        game.playerHasLeft(playerID);
        return;
      case SET_PLAYER:
        game.setPlayer(((ConcreteSetPlayer) request).getAge());
        return;
      case GET_HAND:
        server.sendToPlayer(playerID, new HandInfo(game.getPlayerHandWidth(playerID),
            game.getPlayerHandHeight(playerID), game.getPlayerStones(playerID)));
      case GET_TABLE:
        server.sendToPlayer(playerID, new TableInfo(game.getTableWidth(), game.getTableHeight(), game.getTableStones()));
      default:
    }
  }
}

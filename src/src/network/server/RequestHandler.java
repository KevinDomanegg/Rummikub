package network.server;
//Might be better to move this class to the game-package
import communication.gameinfo.GridInfo;
import communication.gameinfo.HandInfo;
import communication.gameinfo.StoneInfo;
import communication.gameinfo.TableInfo;
import communication.request.*;
import game.Coordinate;
import game.Game;
import game.Stone;
import java.util.Map;

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

  private static StoneInfo[][] parseStoneInfoGrid(int width, int height, Map<Coordinate, Stone> stones) {
    StoneInfo[][] grid = new StoneInfo[width][height];
    Stone stone;
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        stone = stones.get(new Coordinate(i, j));
        if (stone != null) {
          grid[i][j] = new StoneInfo(stone.getColor().toString(), stone.getNumber());
        }
      }
    }
    return grid;
  }

  public void applyRequest(Request request, int playerID){
    RequestID id = request.getRequestID();

    switch (id){
      case START:
        game.start();
        server.sendToAll(new TableInfo(parseStoneInfoGrid(game.getTableWidth(), game.getTableHeight(), game.getTableStones())));
        for (int i = 0; i < game.getNumberOfPlayers(); i++) {
          server.sendToPlayer(i, new HandInfo(parseStoneInfoGrid(game.getPlayerHandWidth(i),
              game.getPlayerHandHeight(i), game.getPlayerStones(i))));
        }
        return;
      case HAND_MOVE:
        ConcreteHandMove handMove = (ConcreteHandMove) request;
        game.moveStoneOnHand(playerID,
            new Coordinate(handMove.getInitCol(), handMove.getInitRow()),
            new Coordinate(handMove.getTargetCol(), handMove.getTargetRow()));
        return;
      case TABLE_MOVE:
        ConcreteTableMove tableMove = (ConcreteTableMove) request;
        game.moveStoneOnTable(new Coordinate(tableMove.getInitCol(), tableMove.getInitRow()),
            new Coordinate(tableMove.getTargetCol(), tableMove.getTargetRow()));
        return;
      case PUT_STONE:
        ConcretePutStone putStone = (ConcretePutStone) request;
        game.moveStoneFromHand(new Coordinate(putStone.getInitCol(), putStone.getInitRow()),
            new Coordinate(putStone.getTargetCol(), putStone.getTargetRow()));
        return;
      case DRAW:
        game.drawStone();
        return;
      case CONFIRM_MOVE:
        //if (game.isConsistent()) {
          server.sendToAll(new TableInfo(parseStoneInfoGrid(game.getTableWidth(), game.getTableHeight(), game.getTableStones())));
        //}
        return;
      case GIVE_UP:
        game.playerHasLeft(playerID);
        return;
      case SET_PLAYER:
        game.setPlayer(((ConcreteSetPlayer) request).getAge());
        return;
      case GET_HAND:
        server.sendToPlayer(playerID,
            new HandInfo(parseStoneInfoGrid(game.getPlayerHandWidth(playerID),
            game.getPlayerHandHeight(playerID), game.getPlayerStones(playerID))));
        return;
      case GET_TABLE:
        server.sendToPlayer(playerID, new TableInfo(
            parseStoneInfoGrid(game.getTableWidth(), game.getTableHeight(), game.getTableStones())));
      default:
    }
  }
}

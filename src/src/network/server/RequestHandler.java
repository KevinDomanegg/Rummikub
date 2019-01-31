package network.server;
//Might be better to move this class to the game-package
import communication.gameinfo.BagInfo;
import communication.gameinfo.CurrentPlayerInfo;
import communication.gameinfo.ErrorInfo;
import communication.gameinfo.GameInfoID;
import communication.gameinfo.GameStartInfo;
import communication.gameinfo.GridInfo;
import communication.gameinfo.HandSizesInfo;
import communication.gameinfo.PlayerNamesInfo;
import communication.gameinfo.RankInfo;
import communication.gameinfo.SimpleGameInfo;
import communication.gameinfo.StoneInfo;
import communication.request.ConcreteMove;
import communication.request.ConcreteSetPlayer;
import communication.request.Request;
import game.Coordinate;
import game.Game;
import game.Stone;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class RequestHandler {
  /**
   * Class that can take Request-Objects and transform the into method-calls for a Game.
   * Acts as a Link between RummiServer and Game.
   */

  private static final String NOT_YOUR_TURN = "not your turn!";
  private static final String NOT_ALLOWED_MOVE = "not allowed to move stones like that!";

  private Game game;
  private Server server;

  RequestHandler(Server server, Game game) {
    this.server = server;
    this.game = game;
  }

  private static StoneInfo[][] parseStoneInfoGrid(int width, int height, Map<Coordinate, Stone> stones) {
    StoneInfo[][] grid = new StoneInfo[width][height];
    Stone stone;
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if ((stone = stones.get(new Coordinate(i, j))) != null) {
          grid[i][j] = new StoneInfo(stone.getColor().toString(), stone.getNumber());
        }
      }
    }
    return grid;
  }

  void applyRequest(Object request, int playerID) {
    // for test
    System.out.println("-----------------------" +request);
    switch (((Request) request).getRequestID()) {

      case START:
        // check if the player is the host (host id is always 0)
        if (playerID != 0) {
          sendErrorToPlayer(playerID, "only host can start the game.");
          return;
        }
        // check if there are at least 2 players
        if (game.getNumberOfPlayers() < 2) {
          sendErrorToPlayer(playerID, "wait for other players to join.");
          return;
        }
        // check if the game is already on
        if (!game.start()) {
          sendErrorToPlayer(playerID, "game is already on.");
          return;
        }
        notifyGameStartToAll();
        return;

      case SET_PLAYER:
        ConcreteSetPlayer setPlayer = (ConcreteSetPlayer) request;
        if (!game.setPlayer(playerID, setPlayer.getName(), setPlayer.getAge())) {
          sendErrorToPlayer(playerID, "the game is already full");
          return;
        }
        server.sendToAll(new PlayerNamesInfo(game.getPlayerNames()));
        return;

      case HAND_MOVE:
        ConcreteMove handMove = (ConcreteMove) request;
        game.moveStoneOnHand(playerID,
            new Coordinate(handMove.getInitCol(), handMove.getInitRow()),
            new Coordinate(handMove.getTargetCol(), handMove.getTargetRow()));
        sendHandToPlayer(playerID);
      return;

      case HAND_SET_MOVE:
        ConcreteMove handSetMove = (ConcreteMove) request;
        if (!game.moveSetOnHand(playerID,
            new Coordinate(handSetMove.getInitCol(), handSetMove.getInitRow()),
            new Coordinate(handSetMove.getTargetCol(), handSetMove.getTargetRow()))) {
          sendErrorToPlayer(playerID, NOT_ALLOWED_MOVE);
        }
        sendHandToPlayer(playerID);
        return;

      case TABLE_MOVE:
        if (isCurrentPlayer(playerID)) {
          ConcreteMove tableMove = (ConcreteMove) request;
          game.moveStoneOnTable(new Coordinate(tableMove.getInitCol(), tableMove.getInitRow()),
              new Coordinate(tableMove.getTargetCol(), tableMove.getTargetRow()));
        }
        sendTableToALl();
        return;

      case TABLE_SET_MOVE:
        if (isCurrentPlayer(playerID)) {
          ConcreteMove tableMove = (ConcreteMove) request;
          if (!game.moveSetOnTable(new Coordinate(tableMove.getInitCol(), tableMove.getInitRow()),
              new Coordinate(tableMove.getTargetCol(), tableMove.getTargetRow()))) {
            sendErrorToPlayer(playerID, NOT_ALLOWED_MOVE);
          }
        }
        sendTableToALl();
        return;

      case PUT_STONE:
        if (isCurrentPlayer(playerID)) {
          ConcreteMove putStone = (ConcreteMove) request;
          if (!game.putStone(new Coordinate(putStone.getInitCol(), putStone.getInitRow()),
              new Coordinate(putStone.getTargetCol(), putStone.getTargetRow()))) {
            sendErrorToPlayer(playerID, NOT_ALLOWED_MOVE);
          }
          sendHandSizesToAll();
        }
        sendTableToALl();
        sendHandToPlayer(playerID);
        return;

      case PUT_SET:
        if (isCurrentPlayer(playerID)) {
          ConcreteMove putSet = (ConcreteMove) request;
          if (!game.putSet(new Coordinate(putSet.getInitCol(), putSet.getInitRow()),
              new Coordinate(putSet.getTargetCol(), putSet.getTargetRow()))) {
            sendErrorToPlayer(playerID, NOT_ALLOWED_MOVE);
          }
          sendHandSizesToAll();
        }
        sendTableToALl();
        sendHandToPlayer(playerID);
        return;

      case DRAW:
        if (isCurrentPlayer(playerID)) {
          if (game.getBagSize() == 0) {
            sendErrorToPlayer(playerID, "Bag is empty!");
            return;
          }
          game.reset();
          game.drawStone();
          // send the player new hand with a drawn stone
          sendHandToPlayer(playerID);
          // send the original table to all
          sendTableToALl();
          sendHandSizesToAll();
          sendBagSizeToAll();
          notifyTurnToPlayer();
        }
      return;

      case CONFIRM_MOVE:
        if (isCurrentPlayer(playerID)) {
          if (game.isConsistent()) {
            checkWinner();
            notifyTurnToPlayer();
            sendHandSizesToAll();
          } else {
            sendErrorToPlayer(playerID, (game.hasPlayerPlayedFirstMove(playerID))
                ? "invalid move!"
                : "sum of stone-values at the first move must be at least 30 points!");
          }
        }
        return;

      case RESET:
        if (isCurrentPlayer(playerID)) {
          game.reset();
//        sendTableToPlayer(playerID);
          sendTableToALl();
          sendHandToPlayer(playerID);
          sendHandSizesToAll();
//        sendHandSizesToPlayer(playerID);
        }
      return;
      case TIME_OUT:
          // sends original table
          game.reset();
          sendTableToALl();
          sendHandToPlayer(playerID);
          // draw stone cause table not consistent and the time is out
          game.drawStone();
          sendHandToPlayer(playerID);
          // send changed hand to player
          sendHandSizesToAll();
          notifyTurnToPlayer();
        return;

      case SORT_HAND_BY_GROUP:
        game.sortPlayerHandByGroup(playerID);
        sendHandToPlayer(playerID);
        return;

      case SORT_HAND_BY_RUN:
        game.sortPlayerHandByRun(playerID);
        sendHandToPlayer(playerID);
        return;

      case UNDO:
        if (isCurrentPlayer(playerID)) {
          game.undo();
          sendTableToALl();
          sendHandToPlayer(playerID);
        }
        return;
      default:
    }
  }

  private boolean isCurrentPlayer(int playID) {
    if (game.getCurrentPlayerID() != playID) {
      sendErrorToPlayer(playID, NOT_YOUR_TURN);
      return false;
    }
    return true;
  }

  private void sendTableToPlayer(int playerID) {
    server.sendToPlayer(playerID, new GridInfo(GameInfoID.TABLE, parseStoneInfoGrid(game.getTableWidth(), game.getTableHeight(), game.getTableStones())));
  }

  private void notifyTurnToPlayer() {

    int currentPlayerID = game.getCurrentPlayerID();

    for (int i = 0; i < 4; i++) {
      if (i == currentPlayerID) {
        // Tells player that it is his turn
        server.sendToPlayer(i, new SimpleGameInfo(GameInfoID.YOUR_TURN));
      } else {
        // Tells other players whose turn it is
        int relativeID = calculateRelativeID(i, currentPlayerID);
        server.sendToPlayer(i, new CurrentPlayerInfo(relativeID));
      }
    }
  }

  private int calculateRelativeID(int recipientID, int currentPlayerID) {

    int relativeID;
    int numOfPlayers = game.getNumberOfPlayers();

    relativeID = currentPlayerID - recipientID;
    if (relativeID < 0) {
      relativeID = numOfPlayers + relativeID;
    }

    return relativeID;
//    if (numOfPlayers == 4) {
//      return relativeID;
//    }
//
//    if (numOfPlayers == 3) {
//      if (relativeID == 2) {
//        relativeID = 3;
//        return relativeID;
//      } else {
//        return relativeID;
//      }
//    }
//
//    if (numOfPlayers == 2) {
//      if (relativeID == 0) {
//        return relativeID;
//      } else {
//        return 2;
//      }
//    }
    //relativeID = relativeID % game.getNumberOfPlayers();

  }

  private void notifyGameStartToAll() {
    server.sendToAll(new GameStartInfo(GameInfoID.GAME_START));
    // send table first to all
    sendTableToALl();
    // send to each player their hand
    for (int playerID = 0; playerID < game.getNumberOfPlayers(); playerID++) {
      sendHandToPlayer(playerID);
    }
    sendPlayerNamesToAll();
    // send to each player their hand sizes in a corresponding order
    sendHandSizesToAll();
    // send bag size to all
    sendBagSizeToAll();
    // notify the start player
    notifyTurnToPlayer();
  }

  private void sendBagSizeToAll() {
    server.sendToAll(new BagInfo(game.getBagSize()));
  }

  private void sendTableToALl() {
    server.sendToAll(new GridInfo(GameInfoID.TABLE, parseStoneInfoGrid(game.getTableWidth(), game.getTableHeight(), game.getTableStones())));
  }

  private void sendHandToPlayer(int playerID) {
    server.sendToPlayer(playerID, new GridInfo(GameInfoID.HAND, parseStoneInfoGrid(game.getPlayerHandWidth(playerID),
        game.getPlayerHandHeight(playerID), game.getPlayerStones(playerID))));
  }

  private void sendHandSizesToAll() {
    List<Integer> handSizes = game.getPlayerHandSizes();
    server.sendToPlayer(0, new HandSizesInfo(handSizes));
    for (int playerID = 1; playerID < game.getNumberOfPlayers(); playerID++) {
      Collections.rotate(handSizes, -1);
      server.sendToPlayer(playerID, new HandSizesInfo(handSizes));
    }
  }

  private void sendPlayerNamesToAll() {
    List<String> names = game.getPlayerNames();
    server.sendToPlayer(0, new PlayerNamesInfo(names));
    for (int playerID = 1; playerID < game.getNumberOfPlayers(); playerID++) {
      Collections.rotate(names, -1);
      server.sendToPlayer(playerID, new PlayerNamesInfo(names));
    }
  }

  private void sendHandSizesToPlayer(int playerID) {
    List<Integer> handSizes = game.getPlayerHandSizes();
    Collections.rotate(handSizes, -playerID);
    server.sendToPlayer(playerID, new HandSizesInfo(handSizes));
  }

  private void checkWinner() {
    if (game.hasWinner()) {
      server.sendToAll(new RankInfo(game.getFinalRank()));
    }
  }

  private void sendErrorToPlayer(int playerID, String message) {
    server.sendToPlayer(playerID, new ErrorInfo(message));
  }
}

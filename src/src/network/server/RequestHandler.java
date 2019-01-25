package network.server;
//Might be better to move this class to the game-package
import communication.gameinfo.*;
import communication.request.*;
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

  void applyRequest(Request request, int playerID){
    switch (request.getRequestID()){
      case START:
        // check the minimum
        if (playerID != 0) {
          server.sendToPlayer(playerID, new ErrorInfo("only host can start the game."));
          return;
        }
        if (game.getNumberOfPlayers() < 2) {
          server.sendToPlayer(playerID, new ErrorInfo("wait for other players to join."));
          return;
        }
        //starts the game
        notifyGameStartToAll();
        startGame();
        return;
      case SET_PLAYER:
        ConcreteSetPlayer setPlayer = (ConcreteSetPlayer) request;
        game.setPlayer(setPlayer.getName(), setPlayer.getAge());
//        sendPlayerNamesToAll();
        server.sendToAll(new PlayerNamesInfo(game.getPlayerNames()));
//        sendUsernames(playerID, ((ConcreteSetPlayer) request).getName());
        return;
      case HAND_MOVE:
      ConcreteMove handMove = (ConcreteMove) request;
      game.moveStoneOnHand(playerID,
            new Coordinate(handMove.getInitCol(), handMove.getInitRow()),
            new Coordinate(handMove.getTargetCol(), handMove.getTargetRow()));
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
      case PUT_STONE:
        if (isCurrentPlayer(playerID)) {
          ConcreteMove putStone = (ConcreteMove) request;
          game.putStone(new Coordinate(putStone.getInitCol(), putStone.getInitRow()),
              new Coordinate(putStone.getTargetCol(), putStone.getTargetRow()));
          sendHandSizesToAll();
        }
        sendTableToALl();
        sendHandToPlayer(playerID);
      return;
      case DRAW:
        if (isCurrentPlayer(playerID)) {
          game.reset();
          game.drawStone();
          // send the player new hand with a drawn stone
          sendHandToPlayer(playerID);
          // send the original table to all
          sendTableToALl();
          sendHandSizesToAll();
          notifyTurnToPlayer();
        }
//        server.sendToAll(new SimpleGameInfo(GameInfoID.DRAW));
      //SEND NEW COUNTDOWN FOR 30 SECONDS
      //sendNewTimer();
      return;
      case CONFIRM_MOVE:
        if (isCurrentPlayer(playerID)) {
          if (game.isConsistent()) {
          /*// send the changed table first
          sendTableToALl();*/
            // then notify the turn to the next player
            notifyTurnToPlayer();
            // sendNewTimer();
          } else {
            // send the original table to all players
            sendTableToALl();
            // send the original hand to the current player
            sendHandToPlayer(playerID);
            // send the original hand sizes to all players
            sendHandSizesToAll();
            // notify wrong move
            server.sendToPlayer(playerID, new ErrorInfo("invalid move!"));
          }
          sendHandSizesToAll();
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
      case GIVE_UP:
      game.playerHasLeft(playerID);
      sendBagSizeToAll();
      sendHandSizesToAll();
      return;
      case TIME_OUT:
        if (game.isConsistent()) {
          /*// send the changed table first
          sendTableToALl();*/
          // then notify the turn to the next player
          notifyTurnToPlayer();
        } else {
          // sends original table
          sendTableToALl();

          sendHandToPlayer(playerID);
          // draw stone cause table not consistent and the time is out
          game.drawStone();
          sendHandToPlayer(playerID);
          // send changed hand to player
          sendHandSizesToAll();
          notifyTurnToPlayer();
        }
        return;
      case SORT_HAND_BY_GROUP:
        game.sortPlayerHandByGroup(playerID);
        sendHandToPlayer(playerID);
        return;
      case SORT_HAND_BY_RUN:
        game.sortPlayerHandByRun(playerID);
        sendHandToPlayer(playerID);
      default:
    }
  }

  private boolean isCurrentPlayer(int playID) {
    if (game.getCurrentPlayerID() != playID) {
      server.sendToPlayer(playID, new ErrorInfo("not your turn!"));
      return false;
    }
    return true;
  }

  private void sendTableToPlayer(int playerID) {
    server.sendToPlayer(playerID, new GridInfo(GameInfoID.TABLE, parseStoneInfoGrid(game.getTableWidth(), game.getTableHeight(), game.getTableStones())));
  }

  private void notifyTurnToPlayer() {
    server.sendToPlayer(game.getCurrentPlayerID(), new SimpleGameInfo(GameInfoID.YOUR_TURN));
    // Notifies the players whose is playing
    server.sendToAll(new CurrentPlayerInfo(game.getCurrentPlayerID()));
  }

  private void startGame() {
    // START THE GAME
    game.start();
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

  private void notifyGameStartToAll() {
    server.sendToAll(new GameStartInfo(GameInfoID.GAME_START));
  }
}

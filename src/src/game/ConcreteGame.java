//package game;
//
//import communication.gameinfo.GameInfoID;
//import communication.gameinfo.Timer;
//import network.server.RummiServer;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * Dummy-Class for testing the Network.
// * Not intended for usage in the final version.
// */
//public class ConcreteGame implements Game {
//
//  private RummiServer server;
//
//  public ConcreteGame(RummiServer server) {
//    this.server = server;
//  }
//
//
//  @Override
//  public void setPlayer(int age) {
//
//  }
//
//  @Override
//  public void start() {
//    server.sendToAll(new Timer(GameInfoID.DRAW));
//  }
//
//  @Override
//  public void moveStoneOnTable(Coordinate currentCoord, Coordinate nextCoord) {
//    server.sendToAll(new Timer(GameInfoID.DRAW));
//  }
//
//  @Override
//  public void moveStoneFromHand(Coordinate currentCoord, Coordinate nextCoord) {
//    server.sendToAll(new Timer(GameInfoID.DRAW));
//  }
//
//  @Override
//  public void moveStoneOnHand(int pos, Coordinate currentCoord, Coordinate nextCoord) {
//    server.sendToAll(new Timer(GameInfoID.DRAW));
//  }
//
//  @Override
//  public void undo() {
//    server.sendToAll(new Timer(GameInfoID.DRAW));
//  }
//
//  @Override
//  public boolean isConsistent() {
//    return false;
//  }
//
//  @Override
//  public void drawStone() {
//    server.sendToAll(new Timer(GameInfoID.DRAW));
//  }
//
//  @Override
//  public void playerHasLeft(int playerPosition) {
//
//  }
//
//
//  @Override
//  public boolean hasWinner() {
//    return false;
//  }
//
//  @Override
//  public Map<Coordinate, Stone> getTableStones() {
//    return null;
//  }
//
//  @Override
//  public Map<Coordinate, Stone> getCurrentPlayerStones() {
//    return null;
//  }
//
//  @Override
//  public List<Integer> getPlayerHandSizes() {
//    return null;
//  }
//
//  @Override
//  public int getBagSize() {
//    return 0;
//  }
//
//  @Override
//  public int getCurrentPlayerPosition() {
//    return 0;
//  }
//
//  @Override
//  public int getTableWidth() {
//    return 0;
//  }
//
//  @Override
//  public int getTableHeight() {
//    return 0;
//  }
//
//  @Override
//  public int getCurrentPlayerHandWidth() {
//    return 0;
//  }
//
//  @Override
//  public int getCurrentPlayerHandHeight() {
//    return 0;
//  }
//}

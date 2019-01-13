package game;

import communication.InfoID;
import communication.Timer;
import network.server.RummiServer;

import java.util.List;
import java.util.Map;

public class ConcreteGame implements Game {
  /**
   * Dummy-Class for testing the Network.
   * Not intended for usage in the final version.
   */

  private RummiServer server;

  public void setserver(RummiServer server) {
    this.server = server;
  }

  public ConcreteGame(RummiServer server) {
    this.server = server;
  }

  @Override
  public void countUpHand() {
    server.sendToAll(new Timer(InfoID.DRAW));
  }

  @Override
  public void countDownHand() {
    server.sendToAll(new Timer(InfoID.DRAW));
  }

  @Override
  public void setStarter(int hand) {
    server.sendToAll(new Timer(InfoID.DRAW));
  }

  @Override
  public void start() {
    server.sendToAll(new Timer(InfoID.DRAW));
  }

  @Override
  public void moveStoneonTable(Coordinate currentCoord, Coordinate nextCoord) {
    server.sendToAll(new Timer(InfoID.DRAW));
  }

  @Override
  public void moveStoneFromHand(Coordinate currentCoord, Coordinate nextCoord) {
    server.sendToAll(new Timer(InfoID.DRAW));
  }

  @Override
  public void moveStoneOnHand(Coordinate currentCoord, Coordinate nextCoord) {
    server.sendToAll(new Timer(InfoID.DRAW));
  }

  @Override
  public void reset(int moves) {
    server.sendToAll(new Timer(InfoID.DRAW));
  }

  @Override
  public void undo() {
    server.sendToAll(new Timer(InfoID.DRAW));
  }

  @Override
  public boolean isConsistent() {
    return false;
  }

  @Override
  public void drawStone() {
    server.sendToAll(new Timer(InfoID.DRAW));
  }

  @Override
  public void handDown() {
    server.sendToAll(new Timer(InfoID.DRAW));
  }

  @Override
  public boolean hasWinnder() {
    return false;
  }

  @Override
  public Map<Coordinate, Stone> getTable() {
    return null;
  }

  @Override
  public Map<Coordinate, Stone> getCurrentHand() {
    return null;
  }

  @Override
  public List<Integer> getOtherHandSizes() {
    return null;
  }

  @Override
  public int getBagSize() {
    return 0;
  }
}

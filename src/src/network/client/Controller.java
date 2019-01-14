package network.client;

import communication.gameinfo.StoneInfo;
import communication.request.ConcreteHandMove;
import communication.request.ConcretePutStone;
import communication.request.ConcreteTableMove;
import communication.request.GetHand;
import communication.request.GetTable;
import communication.request.Start;
import network.server.RummiServer;
import view.DemoView;

public class Controller {

  private RummiClient client;
  DemoView view;

  public Controller(DemoView view) {
    this.view = view;
  }

  public void host(String name, int age) {
    new RummiServer().start();
    join(name, age, "localhost");
  }

  public void join(String name, int age, String serverIP) {
    client = new RummiClient(name, age, serverIP);
    client.setGameInfoHandler(new GameInfoHandler(this));
    client.start();
    System.out.println("Client:" + name + " started");
  }

  public void printGame() {
    view.printGame();
  }

  void setTable(StoneInfo[][] table) {
    view.setTable(table);
  }

  void setPlayerHand(StoneInfo[][] hand) {
    view.setPlayerHand(hand);
  }

  public void moveStoneOnTable(int initCol, int initRow, int targetCol, int targetRow) {
    view.moveStoneOnTable(initCol, initRow, targetCol, targetRow);
    client.qeueRequest(new ConcreteTableMove(initCol, initRow, targetCol, targetRow));
  }

  public void moveStoneFromHand(int initCol, int initRow, int targetCol, int targetRow) {
    view.moveStoneFromHand(initCol, initRow, targetCol, targetRow);
    client.qeueRequest(new ConcretePutStone(initCol, initRow, targetCol, targetRow));
  }

  public void moveStoneOnHand(int initCol, int initRow, int targetCol, int targetRow) {
    view.moveStoneOnHand(initCol, initRow, targetCol, targetRow);
    client.qeueRequest(new ConcreteHandMove(initCol, initRow, targetCol, targetRow));
  }

  public void startGame() {
    client.qeueRequest(new Start());
  }

}

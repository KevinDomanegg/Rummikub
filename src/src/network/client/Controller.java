package network.client;

import communication.gameinfo.StoneInfo;
import communication.request.*;
import network.server.RummiServer;
import view.DemoView;

public class Controller {

  private RummiClient client;
  private DemoView view;
  private boolean isMyTurn;
  private boolean hasGameStarted;
  private boolean isHost;

  public Controller(DemoView view) {
    this.view = view;
  }

  public void host(String name, int age) {
    isHost = true;
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

  void yourTurn() {
    isMyTurn = true;
    view.printYourTurn(client.getUserName());
  }

  void setTable(StoneInfo[][] table) {
    view.setTable(table);
  }

  void setPlayerHand(StoneInfo[][] hand) {
    view.setPlayerHand(hand);
  }

  void printBagSize(int size) {
    view.printBagSize(size);
  }

  public void moveStoneOnTable(int initCol, int initRow, int targetCol, int targetRow) {
    if (isMyTurn) {
      view.moveStoneOnTable(initCol, initRow, targetCol, targetRow);
      client.qeueRequest(new ConcreteTableMove(initCol, initRow, targetCol, targetRow));
    } else {
      view.printNotYourTurn();
    }
  }

  public void moveStoneFromHand(int initCol, int initRow, int targetCol, int targetRow) {
    if (isMyTurn) {
      view.moveStoneFromHand(initCol, initRow, targetCol, targetRow);
      client.qeueRequest(new ConcretePutStone(initCol, initRow, targetCol, targetRow));
    } else {
      view.printNotYourTurn();
    }
  }

  public void moveStoneOnHand(int initCol, int initRow, int targetCol, int targetRow) {
    view.moveStoneOnHand(initCol, initRow, targetCol, targetRow);
    client.qeueRequest(new ConcreteHandMove(initCol, initRow, targetCol, targetRow));
  }

  public void sendCheck() {
    client.qeueRequest(new ConfrimMoveRequest());
  }

  public void startGame() {
    if (isHost && !hasGameStarted) {
      client.qeueRequest(new Start());
      hasGameStarted = true;
    } else {
      view.printNotHost();
    }
  }

  /*public void draw() {
    if (isMyTurn) {
      client.qeueRequest(new DrawRequest());
    } else {
      view.printNotYourTurn();
    }
  }*/
}

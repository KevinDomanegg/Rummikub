package network.client;

import communication.gameinfo.StoneInfo;
import communication.request.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;
import network.server.RummiServer;
import view.DemoView;

public class ShellController {

  private RummiClient client;
  private DemoView view;
  private boolean isHost;
  private boolean isYourTurn;
  private boolean isGameStarted;

  public ShellController(DemoView view) {
    this.view = view;
  }

  public void host(String name, int age) {
    new RummiServer().start();
    try {
      view.printHostIP(Inet4Address.getLocalHost().getHostAddress());
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    join(name, age, "localhost");
    isHost = true;
  }

  public void join(String name, int age, String serverIP) {
    client = new RummiClient(name, age, serverIP);
    client.setGameInfoHandler_Shell(new GameInfoHandler_Shell(this));
    client.start();
    System.out.println("Client:" + name + " started");
  }

  public void printGame() {
    view.printGame();
  }

  public void moveStoneOnTable(int initCol, int initRow, int targetCol, int targetRow) {
    if (isYourTurn) {
      view.moveStoneOnTable(initCol, initRow, targetCol, targetRow);
      client.qeueRequest(new ConcreteTableMove(initCol, initRow, targetCol, targetRow));
    } else {
      view.printNotYourTurn();
    }
  }

  public void moveStoneFromHand(int initCol, int initRow, int targetCol, int targetRow) {
    if (isYourTurn) {
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
    if (isYourTurn) {
      isYourTurn = false;
      client.qeueRequest(new ConfrimMoveRequest());
    } else {
      view.printNotYourTurn();
    }
  }

  public void startGame() {
    if (isHost) {
      if (isGameStarted) {
        view.printGameAlreadyStarted();
      } else {
        client.qeueRequest(new Start());
        isGameStarted = true;
      }
    } else {
      view.printNotHost();
    }
  }

  public void disconnectClient() {
    client.disconnect();
  }

  void notifyTurn() {
    isYourTurn = true;
    view.printYourTurn(client.getUserName());
  }

  void setTable(StoneInfo[][] table) {
    view.setTable(table);
  }

  void setPlayerHand(StoneInfo[][] hand) {
    view.setPlayerHand(hand);
  }

  void setBagSize(int size) {
    view.setBagSize(size);
  }

  void setHandSizes(List<Integer> handSizes) {
    view.setHandSizes(handSizes);
  }

  public void draw() {
    if (isYourTurn) {
      client.qeueRequest(new DrawRequest());
      isYourTurn = false;
    } else {
      view.printNotYourTurn();
    }
  }

  void printWrongMove() {
    isYourTurn = true;
    view.printWrongMove();
  }

  void countDownBagSize() {
    view.countDownBagSize();
  }

  public void reset() {
    client.qeueRequest(new ResetRequest());
  }
}

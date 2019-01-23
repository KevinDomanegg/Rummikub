package network.client;

import communication.request.*;

public class RequestBuilder {

  private RummiClient client;

  public RequestBuilder(RummiClient client) {
    this.client = client;
  }

  public void sendStartRequest() {
    client.sendRequest(new SimpleRequest(RequestID.START));
  }

  public void sendDrawRequest() {
    client.sendRequest(new SimpleRequest(RequestID.DRAW));
  }

  public void sendResetRequest() {
    client.sendRequest(new SimpleRequest(RequestID.RESET));
  }

  public void sendMoveStoneOnTable(int initCol, int initRow, int targetCol, int targetRow) {
      client.sendRequest(new ConcreteMove(RequestID.TABLE_MOVE, initCol, initRow, targetCol, targetRow));
  }

  public void sendPutStoneRequest(int initCol, int initRow, int targetCol, int targetRow) {
      client.sendRequest(new ConcreteMove(RequestID.PUT_STONE, initCol, initRow, targetCol, targetRow));
  }

  public void moveStoneOnHand(int initCol, int initRow, int targetCol, int targetRow) {
    client.sendRequest(new ConcreteMove(RequestID.HAND_MOVE, initCol, initRow, targetCol, targetRow));
  }

  public void sendConfirmMoveRequest() {
    client.sendRequest(new SimpleRequest(RequestID.CONFIRM_MOVE));
  }

  public void sendSetPlayerRequest(String username, int age) {
    client.sendRequest(new ConcreteSetPlayer(username, age));
  }

  public void sendTimeOutRequest() {
    client.sendRequest(new SimpleRequest(RequestID.TIME_OUT));
  }


}

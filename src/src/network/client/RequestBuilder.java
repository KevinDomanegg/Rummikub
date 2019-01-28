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

  public void sendMoveStoneOnTable(int srcCol, int srcRow, int targetCol, int targetRow) {
      client.sendRequest(new ConcreteMove(RequestID.TABLE_MOVE, srcCol, srcRow, targetCol, targetRow));
  }

  public void sendPutStoneRequest(int srcCol, int srcRow, int targetCol, int targetRow) {
      client.sendRequest(new ConcreteMove(RequestID.PUT_STONE, srcCol, srcRow, targetCol, targetRow));
  }

  public void moveStoneOnHand(int srcCol, int srcRow, int targetCol, int targetRow) {
    client.sendRequest(new ConcreteMove(RequestID.HAND_MOVE, srcCol, srcRow, targetCol, targetRow));
  }

  public void sendMoveSetOnHand(int srcCol, int srcRow, int targetCol, int targetRow) {
    client.sendRequest(new ConcreteMove(RequestID.HAND_SET_MOVE, srcCol, srcRow, targetCol, targetRow));
  }

  public void sendPutSetRequest(int srcCol, int srcRow, int targetCol, int targetRow) {
    client.sendRequest(new ConcreteMove(RequestID.PUT_SET, srcCol, srcRow, targetCol, targetRow));
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


  public void sendSortHandByGroupRequest() {
    client.sendRequest(new SimpleRequest(RequestID.SORT_HAND_BY_GROUP));
  }

  public void sendSortHandByRunRequest() {
    client.sendRequest(new SimpleRequest(RequestID.SORT_HAND_BY_RUN));
  }
}

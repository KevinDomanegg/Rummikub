package communication.request;

import java.io.Serializable;

/**
 * represents all movement in game.
 */
public final class ConcreteMove implements Request, Serializable {
  private final RequestID id;
  private final int initCol;
  private final int initRow;
  private final int targetCol;
  private final int targetRow;

  public ConcreteMove(RequestID id, int initCol, int initRow, int targetCol, int targetRow) {
    this.id = id;
    this.initCol = initCol;
    this.initRow = initRow;
    this.targetCol = targetCol;
    this.targetRow = targetRow;
  }

  public int getInitCol() {
    return initCol;
  }
  public int getInitRow() {
    return initRow;
  }
  public int getTargetCol() {
    return targetCol;
  }
  public int getTargetRow() {
    return targetRow;
  }

  @Override public RequestID getRequestID() {
    return id;
  }
}

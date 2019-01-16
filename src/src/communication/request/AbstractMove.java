package communication.request;

import java.io.Serializable;

/**
 * represents all movement in game.
 */
abstract class AbstractMove implements Serializable {
  private int initCol;
  private int initRow;
  private int targetCol;
  private int targetRow;

  public AbstractMove(int initCol, int initRow, int targetCol, int targetRow) {
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
}

package communication.request;

import game.Coordinate;

/**
 * represents the move from a player.
 */
public class ConcretePutStone extends AbstractMove implements Request {
  public ConcretePutStone(int initCol, int initRow, int targetCol, int targetRow) {
    super(initCol, initRow, targetCol, targetRow);
  }

  /**
   * @return RequestID to identify the concrete implementation of the interface.
   */
  @Override
  public RequestID getRequestID() {
    return RequestID.PUT_STONE;
  }
}

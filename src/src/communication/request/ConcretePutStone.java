package communication.request;

import game.Coordinate;

/**
 * represents the move from a player
 */
public class ConcretePutStone extends AbstractMove implements Request {

  public ConcretePutStone(Coordinate initialCoordinate, Coordinate targetCoordinate) {
    super(initialCoordinate, targetCoordinate);
  }

  /**
   * @return RequestID to identify the concrete implementation of the interface.
   */
  @Override
  public RequestID getRequestID() {
    return RequestID.PUT_STONE;
  }
}

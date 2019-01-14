package communication.request;

import game.Coordinate;

public class ConcreteTableMove extends AbstractMove implements Request {

  public ConcreteTableMove(Coordinate initialCoordinate, Coordinate targetCoordinate) {
    super(initialCoordinate, targetCoordinate);
  }

  /**
   * Dummy-Class for testing the Network.
   * Not intended for usage in the final version.
   */

  /**
   * @return RequestID to identify the concrete implementation of the interface.
   */
  @Override
  public RequestID getRequestID() {
    return RequestID.TABLE_MOVE;
  }

}

package communication.request;

import game.Coordinate;

public class ConcreteHandMove extends AbstractMove implements Request {

  public ConcreteHandMove(Coordinate initialCoordinate, Coordinate targetCoordinate) {
    super(initialCoordinate, targetCoordinate);
  }


  /**
   * @return RequestID to identify the concrete implementation of the interface.
   */
  @Override
  public RequestID getRequestID() {
    return RequestID.HAND_MOVE;
  }

}

package communication.request;

/**
 * stone movement only on player hand.
 */
public class ConcreteHandMove extends AbstractMove implements Request {
  public ConcreteHandMove(int initCol, int initRow, int targetCol, int targetRow) {
    super(initCol, initRow, targetCol, targetRow);
  }

  /**
   * @return RequestID to identify the concrete implementation of the interface.
   */
  @Override
  public RequestID getRequestID() {
    return RequestID.HAND_MOVE;
  }

}

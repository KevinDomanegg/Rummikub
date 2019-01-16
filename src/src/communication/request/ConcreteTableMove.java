package communication.request;

/**
 * stone movement only on table.
 */
public class ConcreteTableMove extends AbstractMove implements Request {

  public ConcreteTableMove(int initCol, int initRow, int targetCol, int targetRow) {
    super(initCol, initRow, targetCol, targetRow);
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

package communication.request;

//@ToDO
//Should this class be deleted?
public class UpdatePlayersRequest implements Request {
  /**
   * @return RequestID to identify the concrete implementation of the interface.
   */
  @Override
  public RequestID getRequestID() {
    return RequestID.UPDATE_PLAYERS;
  }
}

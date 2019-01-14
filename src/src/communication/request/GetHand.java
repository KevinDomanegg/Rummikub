package communication.request;

import java.io.Serializable;

/**
 * request for player hand to update.
 */
public class GetHand implements Request, Serializable {

  @Override
  public RequestID getRequestID() {
    return RequestID.GET_HAND;
  }
}

package communication.request;

import java.io.Serializable;

public class GetHand implements Request, Serializable {

  @Override
  public RequestID getRequestID() {
    return RequestID.GET_HAND;
  }
}

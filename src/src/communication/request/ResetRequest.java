package communication.request;

import java.io.Serializable;

public class ResetRequest implements Request, Serializable {

  @Override
  public RequestID getRequestID() {
    return RequestID.RESET;
  }
}

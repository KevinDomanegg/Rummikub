package communication.request;

import java.io.Serializable;

public class Start implements Request, Serializable {

  @Override public RequestID getRequestID() {
    return RequestID.START;
  }
}

package communication.request;

import java.io.Serializable;

public class DrawRequest implements Request, Serializable {

  @Override
  public RequestID getRequestID() {
    return RequestID.DRAW;
  }
}

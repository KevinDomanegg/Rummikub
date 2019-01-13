package communication.request;

import java.io.Serializable;

public class ConcreteStart implements Request, Serializable {

  @Override
  public RequestID getRequestID() {
    return RequestID.START;
  }
}

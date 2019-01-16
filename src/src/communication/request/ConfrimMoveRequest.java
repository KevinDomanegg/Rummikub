package communication.request;

import java.io.Serializable;

public class ConfrimMoveRequest implements Request, Serializable {

  @Override
  public RequestID getRequestID() {
    return RequestID.CONFIRM_MOVE;
  }
}

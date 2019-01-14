package communication.request;

import java.io.Serializable;

/**
 * request to start game.
 */
public class Start implements Request, Serializable {

  @Override public RequestID getRequestID() {
    return RequestID.START;
  }
}

package communication.request;

import java.io.Serializable;

/**
 * request for game table for update.
 */
public class GetTable implements Request, Serializable {

  @Override
  public RequestID getRequestID() {
    return RequestID.GET_TABLE;
  }
}

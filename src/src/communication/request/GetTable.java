package communication.request;

import java.io.Serializable;

public class GetTable implements Request, Serializable {

  @Override
  public RequestID getRequestID() {
    return RequestID.GET_TABLE;
  }
}

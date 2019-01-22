package communication.request;

import org.omg.CORBA.TIMEOUT;

import java.io.Serializable;

public class TimeOut implements Request, Serializable {


  @Override
  public RequestID getRequestID() {
    return RequestID.TIME_OUT;
  }
}

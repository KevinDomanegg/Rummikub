package communication.request;

import java.io.Serializable;

public class ConcreteSetPlayer implements Request, Serializable {
  private int age;

  public ConcreteSetPlayer(int age) {
    this.age = age;
  }

  @Override
  public RequestID getRequestID() {
    return RequestID.SET_PLAYER;
  }

  public int getAge() {
    return age;
  }

}

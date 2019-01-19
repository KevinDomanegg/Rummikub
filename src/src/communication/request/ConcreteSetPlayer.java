package communication.request;

import java.io.Serializable;

/**
 * request to set a new player with given age.
 */
public final class ConcreteSetPlayer implements Request, Serializable {
  private final String name;
  private final int age;

  public ConcreteSetPlayer(String name, int age) {
    this.name = name;
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

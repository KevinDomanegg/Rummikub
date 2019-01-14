package communication.request;

public class ConcreteSetPlayer implements Request {
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

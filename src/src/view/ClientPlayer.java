package view;

public class ClientPlayer {

  private int age;
  private String name;
  private int stoneCount;

  ClientPlayer(int age, String name) {
    this.age =  age;
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public String getName() {
    return name;
  }

  public int getStoneCount() {
    return stoneCount;
  }


}
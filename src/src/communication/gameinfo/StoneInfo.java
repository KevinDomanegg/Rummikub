package communication.gameinfo;

import java.io.Serializable;

public class StoneInfo implements Serializable {
  private String color;
  private int number;

  public StoneInfo(String color, int number) {
    this.color = color;
    this.number = number;
  }

  public String getColor() {
    return color;
  }

  public int getNumber() {
    return number;
  }
}

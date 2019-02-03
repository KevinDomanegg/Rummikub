package game;

import globalconstants.Constants;

public class Stone {
  public enum Color { RED, BLACK, YELLOW, BLUE, JOKER }
  public static final int MAX_VALUE = 13;
  public static final int MIN_VALUE = 1;

  private final Color color;
  private final int number;

  public Stone(Color color, int number) {
    this.color = color;
    if(color.equals(Color.JOKER)){
      this.number = Constants.JOKER_POINTS;
    } else {
      this.number = number;
    }
  }

  public Stone() {
    color = Color.JOKER;
    number = Constants.JOKER_POINTS;
  }

  public Color getColor() {
    return color;
  }

  public int getNumber() {
    return number;
  }

  // Testmethods
  @Override
  public String toString() {
    return "(Color: " + color + ", " + "Number: " + " " + number +")";
  }
}
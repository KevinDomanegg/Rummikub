package game;

public class Stone {
  public enum Color { RED, BLACK, YELLOW, BLUE, JOKER }

  private final int JOKER_POINTS = 20;
  private final Color color;
  private final int number;

  public Stone(Color color, int number) {
    this.color = color;
    this.number = number;
  }

  public Stone() {
    color = Color.JOKER;
    number = JOKER_POINTS;
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

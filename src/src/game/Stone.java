package game;


public class Stone {
  public enum Color { RED, BLACK, YELLOW, BLUE, JOKER }

  private Color color;
  private int number;

  public Stone(Color color, int number) {
    //  Stone(Color.JOKER, 0) <=> Stone()
    this.color = color;
    this.number = number;
  }


  public Stone() {
    color = Color.JOKER;
  }

  public Color getColor() {
    return color;
  }

  public int getNumber() {
    return number;
  }


  //Testmethods

  @Override
  public String toString() {
    return "(Color: " + color + "," + "Number: " + " " + number +")";
  }
}

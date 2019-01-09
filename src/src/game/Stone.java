package game;


class Stone {
  enum Color { RED, BLACK, YELLOW, BLUE, JOKER }

  private Color color;
  private int number;

  Stone(Color color, int number) {
    //  Stone(Color.JOKER, 0) <=> Stone()
    this.color = color;
    this.number = number;
  }


  Stone() {
    color = Color.JOKER;
  }

  Color getColor() {
    return color;
  }

  int getNumber() {
    return number;
  }
}

package game;

class Stone {
  enum Type { RED, BLACK, YELLOW, BLUE, JOKER }

  private Type type;
  private int number;

  Stone(Type color, int number) {
    type = color;
    this.number = number;
  }

  Stone() {
    type = Type.JOKER;
  }

  Type getColor() {
    return type;
  }

  int getNumber() {
    return number;
  }
}

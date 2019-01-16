package game;

import java.util.Map;

class Player {
  private final int age;
  private RummiHand hand;

  Player(int age) {
    this.age = age;
    hand = new RummiHand();
  }

  int getAge() {
    return age;
  }

  Map<Coordinate, Stone> getStones() {
    return hand.getStones();
  }

  void pushStone(Stone stone) {
    hand.setStone(nextFreeCoordinate(hand.getStones()), stone);
  }

  private Coordinate nextFreeCoordinate(Map<Coordinate, Stone> stones){
    Coordinate coordinate;
    for (int x = 0; x < hand.getWidth(); x++){
      for (int y = 0; y < hand.getHeight(); y++){
        coordinate = new Coordinate(x, y);
        if (!stones.containsKey(coordinate)) {
          return coordinate;
        }
      }
    }
    return null;
  }

  public void moveStone(Coordinate initialPosition, Coordinate targetPosition) {
    hand.setStone(targetPosition, hand.getStones().remove(initialPosition));
//    Stone movedStone = currentHand.getStones().get(initialPosition);
//
//    currentHand.setStone(targetPosition, movedStone);
//    currentHand.getStones().remove(initialPosition);
  }

  public Stone popStone(Coordinate initialPosition) {
    return hand.getStones().remove(initialPosition);
  }

  public int getHandSize() {
    return hand.size();
  }

  // for test
  @Override public String toString() {
    return "Player(" + age + ")";
  }

  public int getHandWidth() {
    return hand.getWidth();
  }

  public int getHandHeight() {
    return hand.getHeight();
  }
}

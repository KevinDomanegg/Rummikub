package game;

import java.util.Map;

class Player {
  private final String name;
  private final int age;
  private RummiHand hand;
  private boolean hasPlayedFirstMove;

  Player(String name, int age) {
    this.name = name;
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

  void moveStone(Coordinate initialPosition, Coordinate targetPosition) {
    hand.setStone(targetPosition, hand.getStones().remove(initialPosition));
//    Stone movedStone = currentHand.getStones().get(initialPosition);
//
//    currentHand.setStone(targetPosition, movedStone);
//    currentHand.getStones().remove(initialPosition);
  }

  Stone popStone(Coordinate initialPosition) {
    return hand.getStones().remove(initialPosition);
  }

  int getHandSize() {
    return hand.size();
  }

  // for test
  @Override public String toString() {
    return "Player(" + age + ")";
  }

  int getHandWidth() {
    return hand.getWidth();
  }

  int getHandHeight() {
    return hand.getHeight();
  }

  void playedFirstMove() {
    hasPlayedFirstMove = true;
  }

  boolean hasPlayedFirstMove() {
    return hasPlayedFirstMove;
  }

  int getPoints() {
    return -hand.getStones().values().stream().mapToInt(Stone::getNumber).sum();
  }

  String getName() {
    return name;
  }
}

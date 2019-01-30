package game;


import game.Stone.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RummiTable implements Grid {
  private static final int WIDTH = 26;
  private static final int HEIGHT = 8;
  private static final int MIN_SET_SIZE = 3;
  private static final int MAX_GROUP_SIZE = 4;

  private Map<Coordinate, Stone> stones;

  RummiTable() {
    stones = new HashMap<>(WIDTH * HEIGHT);
  }

  @Override public Map<Coordinate, Stone> getStones() {
    return stones;
  }

  @Override public void setStone(Coordinate coordinate, Stone stone) {
    if (stone != null) {
      stones.put(coordinate, stone);
    }
  }

  @Override public Stone removeStone(Coordinate coordinate) {
    return  stones.remove(coordinate);
  }

  @Override public int getWidth() {
    return WIDTH;
  }

  @Override public int getHeight() {
    return HEIGHT;
  }

  @Override public void clear() {
    stones.clear();
  }

  public int size() {
    return stones.size();
  }

//  public Coordinate getFirstCoordOfSetAt(Coordinate coordinate) {
//    int col = coordinate.getCol();
//    // find the first stone for a potential set
//    while (stones.containsKey(new Coordinate(col - 1, coordinate.getRow()))) {
//      col--;
//    }
//    return new Coordinate(col, coordinate.getRow());
//  }

  /**
   * checks if all horizontally grouped stones on the table are valid sets.
   * Valid sets are out of at least three stones and called
   * Group (same number and different colors) or
   * Run (same color and sorted number, hereby 1 should come after 13).
   *
   * @return true if only if all horizontally grouped stones are valid group or run
   */
  boolean isConsistent() {
    // check the minimal Condition (:= a valid set has at least 3 stones)
    if (stones.size() < MIN_SET_SIZE) {
      return false;
    }
    // make a copy of all coordinates of stones, in order to remove checked coordinate safely
    HashSet<Coordinate> checkingList = new HashSet<>(stones.keySet());
    int col;
    int setSize = 0;

    for (Coordinate coordinate : stones.keySet()) {
      // check if all coordinates of stones in stones are confirmed (also the current coordinate)
      if (checkingList.isEmpty()) {
        return true;
      }
      // check if the first(current) coordinate of the potential set is already confirmed
      if (checkingList.contains(coordinate)) {
        // the coordinate of the first stone in a potential set
        coordinate = getFirstCoordOfSetAt(coordinate);
        col = coordinate.getCol();
        // count the number of neighbors of the first stone of a potential set
        while (col < WIDTH && checkingList.remove(new Coordinate(col++, coordinate.getRow()))) {
          setSize++;
        }
        // check the minimal condition and the consistency of the potential set
        if (setSize < MIN_SET_SIZE || !isValidSet(setSize, coordinate)) {
          return false;
        }
        setSize = 0; // reset setSize to 0
      }
    }
    return false;
  }

  /**
   * checks the consistency of a potential set on this table
   * starting with the given coordinate until the given setSize.
   *
   * @param setSize the approved size of a potential set to be used for both group- and run-set
   * @param coordinate the coordinate of the first stone of a potential set
   * @return true if only if a valid group-set or run-set is confirmed
   */
  private boolean isValidSet(int setSize, Coordinate coordinate) {
    Stone stone = stones.get(coordinate);
    // find a non-joker stone
    for (int i = 0; i < MIN_SET_SIZE; i++) {
      // leave the loop as soon as the stone is not a joker
      if (stone.getColor() != Color.JOKER) {
        break;
      }
      stone = stones.get(new Coordinate(coordinate.getCol() + i + 1, coordinate.getRow()));
    }
    // check the consistency with the name and the color of the non-joker stone
    return isValidGroup(setSize, coordinate, stone.getNumber())
        || isValidRun(setSize, coordinate, stone.getColor(), stone.getNumber());
  }

  /**
   * checks if neighbored stones on this table from the given coordinate for the given setSize
   * are Group (same number and different color) with the given expectedNumber.
   *
   * @param setSize the number of stones to be check for the validity of a potential Group
   * @param coordinate the coordinate of the first stone of the potential Group
   * @param expectedNumber the number, which stones should share in order to be valid
   * @return true if only if stones from the given coordinate are identified as a valid Group
   */
  private boolean isValidGroup(int setSize, Coordinate coordinate, int expectedNumber) {
    if (setSize > MAX_GROUP_SIZE) {
      return false;
    }
    Stone stone;
    int col = coordinate.getCol();
    int row = coordinate.getRow();
    Color color;
    // checked colors will be stored and compared with next color
    HashSet<Color> checkedColors = new HashSet<>();

    for (int i = 0; i < setSize; i++) {
      stone = stones.get(new Coordinate(col + i, row));
      color = stone.getColor();
      // check if it's a Joker or it has expectedNumber and its color is unique
      if (!(color == Color.JOKER || stone.getNumber() == expectedNumber && checkedColors.add(color))) {
        return false;
      }
    }
    return true;
  }

  /**
   * checks if neighbored stones on this table from the given coordinate for the given setSize
   * are Run (same color and sorted number, hereby 1 should come after 13)
   * with the given expectedColor.
   *
   * @param setSize the number of stones to be check for the validity of a potential Run
   * @param coordinate the coordinate of the first stone of this potential Run
   * @param expectedColor the color, which stones should share in order to be valid
   * @param expectedNumber the number the first non-Joker-stone of this potential Run should have
   * @return true if only if stones from the given coordinate are identified as a valid Run
   */
  private boolean isValidRun(int setSize, Coordinate coordinate, Color expectedColor, int expectedNumber) {
    Stone stone;
    Color color;
    int number;
    int col = coordinate.getCol();
    int row = coordinate.getRow();

    for (int i = 0; i < setSize; i++) {
      stone = stones.get(new Coordinate(col + i, row));
      color = stone.getColor();
      number = stone.getNumber();
      // skip it if it's a Joker
      if (color == Color.JOKER) {
        continue;
      }
      // check if it's the first to be checked or its number matches the expected (previous) number
      if (!(color == expectedColor && number == expectedNumber)) {
        return false;
      }
      // count up the expectedNumber, 1 (min value) should be followed after 13 (max value)
      expectedNumber = (++expectedNumber > Stone.MAX_VALUE) ? Stone.MIN_VALUE : expectedNumber;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    Coordinate coordinate;
    for (int row = 0; row < HEIGHT; row++) {
      for (int col = 0; col < WIDTH; col++) {
        if (stones.containsKey((coordinate = new Coordinate(col, row)))) {
          stringBuilder.append("Coordinate: ").append(coordinate)
              .append(", Stone: ").append(stones.get(coordinate)).append('\n');
        }
      }
    }
    stringBuilder.append(stones.size());
    return stringBuilder.toString();
  }

  public static void main(String[] args) {
    RummiTable table = new RummiTable();
    table.setStone(new Coordinate(3, 3), new Stone(Color.BLACK,12));
    table.setStone(new Coordinate(4, 3), new Stone(Color.BLACK, 13));
    table.setStone(new Coordinate(5, 3), new Stone(Color.BLACK, 1));
    System.out.println(table.isConsistent());
  }
}

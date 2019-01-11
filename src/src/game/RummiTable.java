package game;


import game.Stone.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RummiTable implements Grid{
  private static final int WIDTH = 30;
  private static final int HEIGHT = 30;
  private static final int MIN_SET_SIZE = 3;

  private Map<Coordinate, Stone> grid;

  RummiTable() {
    grid = new HashMap<>(WIDTH * HEIGHT);
  }

  @Override public Map<Coordinate, Stone> getStnes() {
    return grid;
  }

  @Override public void setStone(Coordinate coordinate, Stone stone) {
    grid.put(coordinate, stone);
  }

  @Override public int getWidth() {
    return WIDTH;
  }

  @Override public int getHeight() {
    return HEIGHT;
  }

  @Override public void clear() {
    grid.clear();
  }

  public boolean isConsistent() {
    // check the minimal Condition (:= a valid set has at least 3 stones)
    if (grid.size() < MIN_SET_SIZE) {
      return false;
    }
    // make a copy of the grid.keySet, in order to remove checked coordinate safely
    HashSet<Coordinate> checkingList = new HashSet<>(grid.keySet());
    int col;
    int setSize = 1;

     for (Coordinate coordinate : grid.keySet()) {
       // check if all coordinates of stones in grid are confirmed
      if (checkingList.isEmpty()) {
        return true;
      }
      // check if the first(current) coordinate of the potential set is already confirmed
      if (checkingList.remove(coordinate)) {
        // column index for potentially second coordinate
        col = coordinate.getCol() + 1;
        // count the number of neighbors of the stone at the current coordinate in the same row
        while (col < WIDTH && checkingList.remove(new Coordinate(col, coordinate.getRow()))) {
          setSize++;
          col++;
        }
        // check the minimal condition
        if (setSize < MIN_SET_SIZE) {
          return false;
        }
        // check the consistency of the potential set
        if (!isValidSet(setSize, coordinate)) {
          return false;
        }
        setSize = 1;
      }
    }
    return false;
  }

  /**
   * checks the consistency of a potential set
   * starting with the given coordinate until the given setSize.
   *
   * @param setSize the approved size of a potential set to be used for both group- and run-set
   * @param coordinate the coordinate of the first stone of a potential set
   * @return true if only if a valid group-set or run-set is confirmed
   */
  private boolean isValidSet(int setSize, Coordinate coordinate) {
    Stone stone = grid.get(coordinate);
    // find a non-joker stone
    for (int i = 0; i < MIN_SET_SIZE; i++) {
      // leave the loop as soon as the stone is not a joker
      if (!stone.getColor().equals(Color.JOKER)) {
        break;
      }
      stone = grid.get(new Coordinate(coordinate.getCol() + i + 1, coordinate.getRow()));
    }
    // check the consistency with the name and the color of the non-joker stone
    return isVaildGroup(setSize, coordinate, stone.getNumber())
        || isVaildRun(setSize, coordinate, stone.getColor());
  }

  private boolean isVaildGroup(int setSize, Coordinate coordinate, int expectedNumber) {
    Stone stone;
    int col = coordinate.getCol();
    int row = coordinate.getRow();
    Color color;
    // checked colors will be stored and compared with next color
    HashSet<Color> checkedColors = new HashSet<>();

    for (int i = 0; i < setSize; i++) {
      stone = grid.get(new Coordinate(col + i, row));
      color = stone.getColor();
      // check if it's a Joker or it has expectedNumber and its color is unique
      if (!(color == Color.JOKER || stone.getNumber() == expectedNumber && checkedColors.add(color))) {
        return false;
      }
    }
    return true;
  }

  private boolean isVaildRun(int setSize, Coordinate coordinate, Color expectedColor) {
    Stone stone;
    int col = coordinate.getCol();
    int row = coordinate.getRow();
    int expectedNumber = 0;
    Color color;
    int number;

    for (int i = 0; i < setSize; i++) {
      stone = grid.get(new Coordinate(col + i, row));
      color = stone.getColor();
      number = stone.getNumber();
      // check if it's a Joker or it has expectedColor
      if (!(color == Color.JOKER || color == expectedColor
          // check if it's not the first to be checked and it has expectedNumber
          && expectedNumber != 0 && number == expectedNumber)) {
        return false;
      }
      expectedNumber = number + 1;
    }
    return true;
  }
}

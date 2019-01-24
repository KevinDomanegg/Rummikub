package game;

import game.Stone.Color;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RummiHand implements Grid {
  private static final int HEIGHT = 3;
  private static final int WIDTH = 18;

  private Map<Coordinate, Stone> stones;

  RummiHand(){
    stones = new HashMap<>();
  }

  @Override public void setStone(Coordinate coordinate, Stone stone) {
    if (stone != null) {
      stones.put(coordinate, stone);
    }
  }

  /**
   * gives the removed stone at the given coordinate on this hand.
   *
   * @param coordinate the coordinate of the removed stone
   * @return the removed stone from the given coordinate
   */
  @Override public Stone removeStone(Coordinate coordinate) {
    return stones.remove(coordinate);
  }

  /**
   * gives pairs out of all stones and their associated coordinate on this hand.
   *
   * @return pairs out of all stones and their associated coordinate on this hand
   */
  @Override public Map<Coordinate,Stone> getStones(){
    return stones;
  }

  @Override
  public void clear(){
    stones.clear();
  }

  public int size() {
    return stones.size();
  }

  @Override
  public int getHeight() {
    return HEIGHT;
  }

  @Override
  public int getWidth() {
    return WIDTH;
  }

  /** groups stones with the same number and sorts these groups of stones by their color. */
  void sortByGroup() {
    sortStonesWith((s1, s2) -> {
      int n1 = s1.getNumber();
      int n2 = s2.getNumber();
      return (n1 == n2) ? s1.getColor().compareTo(s2.getColor()) : n1 - n2;
    });
  }

  /** groups stones with the same color and sorts these groups of stones by their number. */
  void sortByRun() {
    sortStonesWith((s1, s2) -> {
      Color c1 = s1.getColor();
      Color c2 = s2.getColor();
      return (c1 == c2) ? s1.getNumber() - s2.getNumber() : c1.compareTo(c2);
    });
  }

  /**
   * sorts stones on this Hand with the given comparator
   * and replaces(fills) stones on this hand in the sorted order row after row
   * starting from the coordinate (0, 0).
   *
   * @param comparator the comparator to be used to compare stones
   */
  private void sortStonesWith(Comparator<Stone> comparator) {
    // check if there are at least 3 stones (if not, there is no meaning to sort)
    if (stones.size() < 3) {
      return;
    }
    // get iterator of the sorted stream of all stones on this hand with the given comparator
    Iterator<Stone> iterator = stones.values().stream().sorted(comparator).iterator();
    // reset stones
    stones = new HashMap<>();
    for (int row = 0; row < HEIGHT; row++) {
      for (int col = 0; col < WIDTH; col++) {
        // check if there is no stone left to be replaced
        if (!iterator.hasNext()) {
          return;
        }
        stones.put(new Coordinate(col, row), iterator.next());
      }
    }
  }

  //Testmethods
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
}

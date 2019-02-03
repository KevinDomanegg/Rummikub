package game;

import java.util.Map;

/**
 * Interface for different types of grids of Stones.
 */
public interface Grid {

  /**
   * Returns all the Stones on the grid.
   *
   * @return Stones on the grid
   */
  Map<Coordinate, Stone> getStones();

  /**
   * Puts a new Stone on the grid.
   *
   * @param coordinate the Stone will be put on on grid.
   * @param stone      to be put on the grid.
   */
  void setStone(Coordinate coordinate, Stone stone);

  /**
   * Removes a Stone from the grid.
   *
   * @param coordinate of the Stone that will be removed
   * @return the Stone on the specified coordinate
   */
  Stone removeStone(Coordinate coordinate);

  /**
   * Return the width of the Grid.
   *
   * @return width of the Grid.
   */
  int getWidth();

  /**
   * Return the height of the Grid.
   *
   * @return height of the Grid.
   */
  int getHeight();

  /**
   * Deletes all Stones on the grid.
   */
  void clear();

  /**
   * Return the left-most coordinate of a set of Stones.
   * In this context a 'Set' of Stones is a number of Stones directly adjacent
   * to one another.
   *
   * @param coordinate of a Stone in the set.
   * @return Coordinate of the left-most Stone of the Set.
   */
  default Coordinate getFirstCoordOfSetAt(Coordinate coordinate) {
    int col = coordinate.getCol();
    // find the first stone for a potential set
    while (getStones().containsKey(new Coordinate(col - 1, coordinate.getRow()))) {
      col--;
    }
    return new Coordinate(col, coordinate.getRow());
  }
}


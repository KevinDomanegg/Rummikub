package game;

import java.util.Objects;

class Coordinate {
  private int col;
  private int row;

  Coordinate(int col, int row) {
    this.col = col;
    this.row = row;
  }

  @Override public boolean equals(Object other) {
    if (!(other instanceof Coordinate)) {
      return false;
    }
    Coordinate otherCoord = (Coordinate) other;
    return col == otherCoord.col && row == otherCoord.row;
  }

  @Override public int hashCode() {
    return Objects.hash(col, row);
  }
}

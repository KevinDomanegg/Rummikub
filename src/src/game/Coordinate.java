package game;

import java.io.Serializable;
import java.util.Objects;

public class Coordinate implements Serializable{
  private int col;
  private int row;

  public Coordinate(int col, int row) {
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

  @Override
  public String toString(){
    return "(Col: " + col + ",Row: " + row + ")";
  }
}

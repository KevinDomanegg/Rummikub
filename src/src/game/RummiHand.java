package game;

import java.util.*;

public class RummiHand {
  private Map<Coordinate, Stone> grid;
  private final int height = 2;
  private final int width = 20;

  public RummiHand(){

  }

  public void setStone(Coordinate coordinate, Stone stone){
    grid.put(coordinate, stone);
  }

  public Map<Coordinate,Stone> getStones(){
    return grid;
  }

  public void clear(){
    grid.clear();
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public int size() {
    return grid.size();
  }

}

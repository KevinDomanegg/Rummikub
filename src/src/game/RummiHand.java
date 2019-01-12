package game;

import java.util.*;

public class RummiHand {
  private Map<Coordinate, Stone> grid;
  private final int height = 2;
  private final int width = 20;

  public RummiHand(){
    grid = new HashMap<>();
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


  //Testmethods

  public String toString() {
    String stonesOnHand = "";
    int handSize = grid.size();

    for (Map.Entry<Coordinate, Stone> entry : grid.entrySet()){
      stonesOnHand = stonesOnHand + "Coordinate: " + entry.getKey().toString() + "; " +
          "Stone: " + entry.getValue().toString() + "\n";
    }

    return "You have " + handSize + " Stone in your hand." + "\n" + stonesOnHand;
  }
}

package game;

import java.util.*;

public class RummiHand implements Grid {
  private Map<Coordinate, Stone> grid;
  private static final int HEIGHT = 2;
  private static final int WIDTH = 20;


  public RummiHand(){
    grid = new HashMap<>();
  }

  @Override
  public void setStone(Coordinate coordinate, Stone stone){
    grid.put(coordinate, stone);
  }

  @Override
  public Map<Coordinate,Stone> getStones(){
    return grid;
  }

  @Override
  public void clear(){
    grid.clear();
  }

  public int size() {
    return grid.size();
  }

  @Override
  public int getHeight() {
    return HEIGHT;
  }

  @Override
  public int getWidth() {
    return WIDTH;
  }

  //Testmethods

  @Override
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

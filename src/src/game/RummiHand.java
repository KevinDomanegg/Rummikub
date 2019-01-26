package game;

import java.util.*;

public class RummiHand implements Grid {
  private Map<Coordinate, Stone> stones;
  private static final int HEIGHT = 2;
  private static final int WIDTH = 20;


  RummiHand(){
    stones = new HashMap<>();
  }

  @Override
  public void setStone(Coordinate coordinate, Stone stone){
    if(stones.size() < HEIGHT * WIDTH){
      stones.put(coordinate, stone);
    }
  }

  @Override public Stone removeStone(Coordinate coordinate) {
    return stones.remove(coordinate);
  }

  @Override
  public Map<Coordinate,Stone> getStones(){
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

  //Testmethods

  @Override
  public String toString() {
    String stonesOnHand = "";
    int handSize = stones.size();

    for (Map.Entry<Coordinate, Stone> entry : stones.entrySet()){
      stonesOnHand = stonesOnHand + "Coordinate: " + entry.getKey().toString() + "; " +
          "Stone: " + entry.getValue().toString() + "\n";
    }

    return "You have " + handSize + " Stone in your hand." + "\n" + stonesOnHand;
  }
}

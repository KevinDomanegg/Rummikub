package game;

import java.util.ArrayList;
import java.util.Random;

public class RummiBag {
  private ArrayList<Stone> stones = new ArrayList<>();
  private Random randomGenerator;
  private final static int STONESOFEACHCOLOR = 13;


  //The bag is initially filled with 13 Numbers of each
  //color and two Jokers.
  public RummiBag() {
    for (int i = 1; i <= STONESOFEACHCOLOR; i++) {
      for (Stone.Color dir : Stone.Color.values()) {
        if (dir != Stone.Color.JOKER){
          stones.add(new Stone(dir, i));
        }
      }
    }
    stones.add(new Stone(Stone.Color.JOKER, 0));
    stones.add(new Stone(Stone.Color.JOKER, 0));

    randomGenerator = new Random();
  }

  //Returns a random Stone.
  public Stone removeStone(){
    int index = randomGenerator.nextInt(stones.size());
    Stone stone = stones.get(index);
    return stone;
  }

  public int size() {
    return stones.size();
  }

  public void addStones(ArrayList<Stone> givenStones){
    stones.addAll(givenStones);
  }
}

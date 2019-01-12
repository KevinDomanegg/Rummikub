package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RummiBag {
  private ArrayList<Stone> stones = new ArrayList<>();
  private Random randomGenerator;
  private final static int STONESOFEACHCOLOR = 13;


  //The bag is initially filled with 13 Numbers of each
  //color and two Jokers.
  RummiBag() {
    for (int i = 1; i <= STONESOFEACHCOLOR; i++) {
      for (Stone.Color dir : Stone.Color.values()) {
        if (dir != Stone.Color.JOKER){
          stones.add(new Stone(dir, i));
          stones.add(new Stone(dir, i));
        }
      }
    }
    stones.add(new Stone(Stone.Color.JOKER, 0));
    stones.add(new Stone(Stone.Color.JOKER, 0));

    randomGenerator = new Random();
  }

  //Returns a random Stone.
  Stone removeStone(){
    int index = randomGenerator.nextInt(stones.size());
    Stone stone = stones.get(index);
    stones.remove(index);
    return stone;
  }

  int size() {
    return stones.size();
  }

  void addStones(Collection<Stone> extraStones){
    this.stones.addAll(extraStones);
  }

  // for test
  @Override
  public String toString(){
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("bag size: ").append(size()).append('\n');
    for (Stone stone : stones) {
      stringBuilder.append('(').append(stone.getColor()).append(", ").append(stone.getNumber())
          .append(")\n");
    }
    return stringBuilder.toString();
  }

  ArrayList<Stone> getStones(){
    return stones;
  }
}

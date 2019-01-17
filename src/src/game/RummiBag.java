package game;

import game.Stone.Color;
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
    for (Color color : Color.values()) {
      if (color != Color.JOKER) {
        for (int i = 1; i <= STONESOFEACHCOLOR; i++) {
          stones.add(new Stone(color, i));
          stones.add(new Stone(color, i));
        }
      }
    }
    stones.add(new Stone());
    stones.add(new Stone());

    randomGenerator = new Random();
  }

  //Returns a random Stone.
  Stone removeStone(){
    return stones.remove(randomGenerator.nextInt(size()));
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

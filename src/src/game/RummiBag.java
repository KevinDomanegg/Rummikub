package game;

import game.Stone.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RummiBag {
  private static final int MAX_BAG_SIZE = 106;
  private ArrayList<Stone> stones;
  private Random randomGenerator;


  //The bag is initially filled with 13 Numbers of each
  //color and two Jokers.
  RummiBag() {
    stones = new ArrayList<>(MAX_BAG_SIZE);
    for (Color color : Color.values()) {
      if (color != Color.JOKER) {
        for (int i = Stone.MIN_VALUE; i <= Stone.MAX_VALUE; i++) {
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

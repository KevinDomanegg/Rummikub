package game;

import java.util.ArrayList;

public class RummiBag {
  private ArrayList<Stone> stones = new ArrayList<>();
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
  }

  public Stone removeStone(){
    return null;
  }

}

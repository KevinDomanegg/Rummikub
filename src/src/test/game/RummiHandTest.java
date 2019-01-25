package game;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RummiHandTest {
  static RummiHand rummiHand1;
  static RummiHand rummiHand2;
  static int countStones;

  @Before
  public  void initAll(){
    rummiHand1 = new RummiHand();
    rummiHand2 = new RummiHand();
    countStones = 0;
  }

  @Test
  public void fillHandTest(){
    System.out.println("Started fillHandTest.");
    for (int i = 0; i < rummiHand1.getWidth(); i++){
      for (int j = 0; j < rummiHand1.getHeight(); j++){
        rummiHand1.setStone(new Coordinate(i,j), new Stone(Stone.Color.JOKER, 1));
      }
    }
    assertTrue(rummiHand1.size() == rummiHand1.getWidth() * rummiHand1.getHeight());

    rummiHand1.clear();
    assertTrue(rummiHand1.size() == 0);
    System.out.println("Ended fillHandTest.");
  }

  @Test
  public void twoStonesOnOneCoordinate(){
    //If there are two stones set on the same Coordinate the old one will be overwritten.
    //Where do we want to fix that? In gamemodel or in the gamehandler.
    System.out.println("Started twoStonesOnOneCoordinate.");
    rummiHand2.setStone(new Coordinate(1,1), new Stone(Stone.Color.JOKER, 1));
    rummiHand2.setStone(new Coordinate(1,1), new Stone(Stone.Color.JOKER, 2));
    assertTrue(rummiHand2.size() == 1);
    System.out.println("Ended twoStonesOnOneCoordinate.");
  }
}
package game;

import org.junit.Test;

import static org.junit.Assert.*;


public class RummiHandTest {

  @Test
  public void fillHandTest(){
    RummiHand rummiHand1 = new RummiHand();

    for (int i = 0; i < rummiHand1.getWidth(); i++){
      for (int j = 0; j < rummiHand1.getHeight(); j++){
        rummiHand1.setStone(new Coordinate(i,j), new Stone(Stone.Color.JOKER, 1));
      }
    }

    assertTrue(rummiHand1.size() == rummiHand1.getWidth() * rummiHand1.getHeight());
    assertTrue(rummiHand1.getStones().size() == rummiHand1.size());

    rummiHand1.removeStone(new Coordinate(0,0));
    assertTrue(rummiHand1.size() == rummiHand1.getWidth() * rummiHand1.getHeight() - 1);

    rummiHand1.clear();
    assertTrue(rummiHand1.size() == 0);

  }

  @Test
  public void twoStonesOnOneCoordinate(){
    //If there are two stones set on the same Coordinate the old one will be overwritten.
    //Where do we want to fix that? In gamemodel or in the gamehandler.
    RummiHand rummiHand2 = new RummiHand();

    rummiHand2.setStone(new Coordinate(1,1), new Stone(Stone.Color.JOKER, 1));
    rummiHand2.setStone(new Coordinate(1,1), new Stone(Stone.Color.JOKER, 2));
    assertTrue(rummiHand2.size() == 1);

  }

  @Test
  public void test1() {
    RummiHand rummiHand2 = new RummiHand();
    rummiHand2.setStone(new Coordinate(1,1), new Stone(Stone.Color.JOKER, 1));
    rummiHand2.setStone(new Coordinate(2,1), new Stone(Stone.Color.JOKER, 2));
    assertEquals(rummiHand2.points(), 20);

    rummiHand2.setStone(new Coordinate(3,1), new Stone(Stone.Color.BLUE, 2));
    assertEquals(rummiHand2.points(), 22);

  }

}
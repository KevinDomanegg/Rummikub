package game;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;



public class RummiTableTest {

  @Test
  public void fillTableTest(){
    RummiTable rummiTable1 = new RummiTable();

    assertEquals(rummiTable1.getStones().size(), 0);

    for (int i = 0; i < rummiTable1.getWidth(); i++){
      for (int j = 0; j < rummiTable1.getHeight(); j++){
        rummiTable1.setStone(new Coordinate(i,j), new Stone(Stone.Color.JOKER, 1));
      }
    }
    assertTrue(rummiTable1.size() == rummiTable1.getWidth() * rummiTable1.getHeight());

    rummiTable1.clear();
    assertTrue(rummiTable1.size() == 0);
  }

  @Test
  public void twoStonesOnOneCoordinate(){
    //If there are two stones set on the same Coordinate the old one will be overwritten.
    //Where do we want to fix that? In gamemodel or in the gamehandler.

    RummiTable rummiTable2 = new RummiTable();

    rummiTable2.setStone(new Coordinate(1,1), new Stone(Stone.Color.JOKER, 1));
    rummiTable2.setStone(new Coordinate(1,1), new Stone(Stone.Color.JOKER, 2));

    assertTrue(rummiTable2.size() == 1);

  }

  @Test
  public void isTableConsistent(){
    RummiTable rummiTable3 = new RummiTable();

    rummiTable3.setStone(new Coordinate(0,0), new Stone(Stone.Color.JOKER, 1 ));
    rummiTable3.setStone(new Coordinate(1,0), new Stone(Stone.Color.RED, 2 ));
    rummiTable3.setStone(new Coordinate(2,0), new Stone(Stone.Color.RED, 3 ));
    rummiTable3.setStone(new Coordinate(0,1), new Stone(Stone.Color.RED, 1 ));
    rummiTable3.setStone(new Coordinate(1,1), new Stone(Stone.Color.BLUE, 1 ));
    rummiTable3.setStone(new Coordinate(2,1), new Stone(Stone.Color.YELLOW, 1 ));

    assertTrue(rummiTable3.isConsistent());
  }

  @Test
  public void isTableNotConsistent(){
    RummiTable rummiTable4 = new RummiTable();

    rummiTable4.setStone(new Coordinate(0,0), new Stone(Stone.Color.RED, 1 ));
    rummiTable4.setStone(new Coordinate(1,0), new Stone(Stone.Color.RED, 2 ));
    rummiTable4.setStone(new Coordinate(2,0), new Stone(Stone.Color.RED, 3 ));
    rummiTable4.setStone(new Coordinate(0,1), new Stone(Stone.Color.RED, 1 ));
    rummiTable4.setStone(new Coordinate(1,1), new Stone(Stone.Color.BLUE, 1 ));
    rummiTable4.setStone(new Coordinate(2,1), new Stone(Stone.Color.YELLOW, 1 ));

    assertTrue(rummiTable4.isConsistent());

    Stone removedStone = rummiTable4.removeStone(new Coordinate(0,0));
    assertTrue(removedStone.getColor() == Stone.Color.RED && removedStone.getNumber() == 1);

    assertFalse(rummiTable4.isConsistent());
  }

  @Test
  public void notEnoughStonesTest() {
    RummiTable rummiTable5 = new RummiTable();
    RummiTable rummiTable6 = new RummiTable();

    rummiTable5.setStone(new Coordinate(0,0), new Stone(Stone.Color.JOKER, 1 ));
    rummiTable5.setStone(new Coordinate(1,0), new Stone(Stone.Color.RED, 2 ));

    assertTrue(rummiTable5.size() == 2);

    assertFalse(rummiTable6.isConsistent());
    assertFalse(rummiTable5.isConsistent());
  }

  @Test
  public void tooManyStonesForGroupTest() {
    RummiTable rummiTable7 = new RummiTable();

    rummiTable7.setStone(new Coordinate(0,0), new Stone(Stone.Color.RED, 0 ));
    rummiTable7.setStone(new Coordinate(1,0), new Stone(Stone.Color.YELLOW, 0 ));
    rummiTable7.setStone(new Coordinate(2,0), new Stone(Stone.Color.BLUE, 0 ));
    rummiTable7.setStone(new Coordinate(3,0), new Stone(Stone.Color.BLACK, 0 ));
    rummiTable7.setStone(new Coordinate(4,0), new Stone(Stone.Color.JOKER, 0 ));

    assertFalse(rummiTable7.isConsistent());
  }

  @Test
  public void notValidRunTest() {
    RummiTable rummiTable8 = new RummiTable();

    rummiTable8.setStone(new Coordinate(0,0), new Stone(Stone.Color.YELLOW, 1 ));
    rummiTable8.setStone(new Coordinate(1,0), new Stone(Stone.Color.YELLOW, 2 ));
    rummiTable8.setStone(new Coordinate(2,0), new Stone(Stone.Color.YELLOW, 3 ));
    rummiTable8.setStone(new Coordinate(3,0), new Stone(Stone.Color.YELLOW, 4 ));
    rummiTable8.setStone(new Coordinate(4,0), new Stone(Stone.Color.YELLOW, 5 ));
    rummiTable8.setStone(new Coordinate(5,0), new Stone(Stone.Color.RED, 6 ));

    assertFalse(rummiTable8.isConsistent());
  }

  @Test
  public void yetAnotherTest(){
    RummiTable table = new RummiTable();
    table.setStone(new Coordinate(0, 0), new Stone(Stone.Color.YELLOW, 5));
    assertEquals("Coordinate: (0, 0), Stone: (Color: YELLOW, Number:  5)" + "\n" + "1", table.toString());
    table.setStone(new Coordinate(1, 0), new Stone(Stone.Color.BLUE, 5));
    table.setStone(new Coordinate(2, 0), new Stone());
    assertTrue(table.isConsistent());
  }
}
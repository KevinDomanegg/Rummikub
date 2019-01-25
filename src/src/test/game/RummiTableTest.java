package game;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RummiTableTest {
  static RummiTable rummiTable1;
  static RummiTable rummiTable2;
  static RummiTable rummiTable3;
  static RummiTable rummiTable4;


  @Before
  public static void initAll(){
    rummiTable1 = new RummiTable();
    rummiTable2 = new RummiTable();
    rummiTable3 = new RummiTable();
  }

  @Test
  public void fillTableTest(){
    System.out.println("Started fillTableTest.");
    for (int i = 0; i < rummiTable1.getWidth(); i++){
      for (int j = 0; j < rummiTable1.getHeight(); j++){
        rummiTable1.setStone(new Coordinate(i,j), new Stone(Stone.Color.JOKER, 1));
      }
    }
    assertTrue(rummiTable1.size() == rummiTable1.getWidth() * rummiTable1.getHeight());

    rummiTable1.clear();
    assertTrue(rummiTable1.size() == 0);
    System.out.println("Ended fillTableTest.");
  }

  @Test
  public void twoStonesOnOneCoordinate(){
    //If there are two stones set on the same Coordinate the old one will be overwritten.
    //Where do we want to fix that? In gamemodel or in the gamehandler.
    System.out.println("Started twoStonesOnOneCoordinate.");
    rummiTable2.setStone(new Coordinate(1,1), new Stone(Stone.Color.JOKER, 1));
    rummiTable2.setStone(new Coordinate(1,1), new Stone(Stone.Color.JOKER, 2));
    assertTrue(rummiTable2.size() == 1);
    System.out.println("Ended twoStonesOnOneCoordinate.");
  }

  @Test
  public void isTableConsistent(){
    rummiTable3.setStone(new Coordinate(0,0), new Stone(Stone.Color.RED, 1 ));
    rummiTable3.setStone(new Coordinate(1,0), new Stone(Stone.Color.RED, 2 ));
    rummiTable3.setStone(new Coordinate(2,0), new Stone(Stone.Color.RED, 3 ));
    rummiTable3.setStone(new Coordinate(0,1), new Stone(Stone.Color.RED, 1 ));
    rummiTable3.setStone(new Coordinate(1,1), new Stone(Stone.Color.BLUE, 1 ));
    rummiTable3.setStone(new Coordinate(2,1), new Stone(Stone.Color.YELLOW, 1 ));

    assertTrue(rummiTable3.isConsistent());
  }

  public void isTableNotConsistent(){
    rummiTable4.setStone(new Coordinate(0,0), new Stone(Stone.Color.RED, 1 ));
    rummiTable4.setStone(new Coordinate(1,0), new Stone(Stone.Color.RED, 2 ));
    rummiTable4.setStone(new Coordinate(2,0), new Stone(Stone.Color.RED, 3 ));
    rummiTable4.setStone(new Coordinate(0,1), new Stone(Stone.Color.RED, 1 ));
    rummiTable4.setStone(new Coordinate(1,1), new Stone(Stone.Color.BLUE, 1 ));
    rummiTable4.setStone(new Coordinate(2,1), new Stone(Stone.Color.YELLOW, 1 ));

    assertTrue(rummiTable4.isConsistent());
  }
}
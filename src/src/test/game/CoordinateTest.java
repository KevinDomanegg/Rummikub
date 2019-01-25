package game;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoordinateTest {
  static Coordinate coordinate1;
  static Coordinate coordinate2;
  static Coordinate coordinate3;

  @Before
  public void initAll(){
    coordinate1 = new Coordinate(1, 4);
    coordinate2 = new Coordinate(1, 5);
    coordinate3 = new Coordinate(1,4);
  }


  //Testing if the equals Method is able to compare two different
  //Coordinates with the same Variables x and y.
  @Test
  public void equalsTest() {
    System.out.println("TestEquals.");
    assert(coordinate1.equals(coordinate3));
    assertEquals(coordinate1, coordinate3);
    assertTrue(coordinate1.equals(coordinate3));
    assertTrue(coordinate2.equals(coordinate2));
    assertTrue(coordinate1.equals(coordinate2) == false);
    System.out.println("EndOfTestEquals.");
  }
}
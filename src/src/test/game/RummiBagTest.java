package game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;

public class RummiBagTest {

  static RummiBag bag1;
  static RummiBag bag2;
  static final int BAG_SIZE = 106;

  @Before
  public void initAll(){
    bag1 = new RummiBag();
    bag2 = new RummiBag();
  }

  @Test
  public void isBagFullTest(){
    assertTrue(bag1.size() == BAG_SIZE);
    assertTrue(bag2.size() == BAG_SIZE);
    for(int i = 0; i < BAG_SIZE; i++ ){
      bag1.removeStone();
      assertTrue(bag1.size() == BAG_SIZE - i - 1);
    }

  }
}
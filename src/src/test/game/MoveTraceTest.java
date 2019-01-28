package game;

import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


public class MoveTraceTest {

  @Test
  public void initTest(){
    MoveTrace trace1 = new MoveTrace("MOVESTONEONHAND", 1, new Coordinate(0,0), new Coordinate(1,1));
    MoveTrace trace2 = new MoveTrace("MOVESTONEONTABLE", new Coordinate(2,2), new Coordinate(3,2));

    assertTrue(trace1.getCommand().equals("MOVESTONEONHAND"));
    assertTrue(trace1.getInitialPosition().equals(new Coordinate(0,0)));
    assertTrue(trace1.getTargetPosition().equals(new Coordinate(1,1)));
    assertTrue(trace1.getPlayerID() == 1);
  }

}
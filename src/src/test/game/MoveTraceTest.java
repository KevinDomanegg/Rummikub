package game;

import static org.junit.Assert.assertTrue;

import game.MoveTrace.Move;
import org.junit.Test;


public class MoveTraceTest {

  @Test
  public void initTest(){
    MoveTrace trace1 = new MoveTrace(Move.PUT_STONE, new Coordinate(0,0), new Coordinate(1,1));
    MoveTrace trace2 = new MoveTrace(Move.TABLE_MOVE, new Coordinate(2,2), new Coordinate(3,2));

    assertTrue(trace1.getMove().equals(Move.PUT_STONE));
    assertTrue(trace1.getSourcePosition().equals(new Coordinate(0,0)));
    assertTrue(trace1.getTargetPosition().equals(new Coordinate(1,1)));
  }

}
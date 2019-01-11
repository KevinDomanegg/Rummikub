package communication;

import game.Coordinate;

import java.util.Random;


public class Concrete_Hand_Move implements Request{
  /**
   * Dummy-Class for testing the Network.
   * Not intended for usage in the final version.
   */


  /**
   * @return RequestID to identify the concrete implementation of the interface.
   */
  @Override
  public RequestID getRequestID() {
    return RequestID.HAND_MOVE;
  }

  public Coordinate getCurrentCoor(){
    Random rand = new Random();
    return new game.Coordinate(rand.nextInt(), rand.nextInt());
  }

  public Coordinate getNewCoor(){
    Random rand = new Random();
    return new game.Coordinate(rand.nextInt(), rand.nextInt());
  }
}

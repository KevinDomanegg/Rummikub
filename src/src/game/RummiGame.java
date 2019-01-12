package game;


//This Class should be relabeled to Game, and the current Game interface needs to be moved.

public class RummiGame {
  private RummiHand[] hands;
  private RummiTable table;
  private RummiBag bag;
  private RummiHand currentHand;


  public void moveStoneFromHand(Coordinate initialPosition, Coordinate targetPosition){
    Stone movedStone = currentHand.getStones().get(initialPosition);

    table.setStone(targetPosition, movedStone);
    currentHand.getStones().remove(initialPosition);
  }

  public void moveStoneOnHand(Coordinate initialPosition, Coordinate targetPosition){
    Stone movedStone = currentHand.getStones().get(initialPosition);

    currentHand.setStone(targetPosition, movedStone);
    currentHand.getStones().remove(initialPosition);
  }

  public void drawStone(){
    Stone stoneFromBag = bag.getStones().get(0);
    Coordinate targetPosition = nextFreeCoordinate(currentHand);

    currentHand.setStone(targetPosition, stoneFromBag);
  }

  private Coordinate nextFreeCoordinate(RummiHand hand){
    int width = hand.getWidth();
    int height = hand.getHeight();

    for (int x = 0; x < width; x++){
      for (int y = 0; y < height; y++){
        if (hand.getStones().containsKey(new Coordinate(x,y)) == false){
          return new Coordinate(x,y);
        }
      }
    }
    return null;
  }



}

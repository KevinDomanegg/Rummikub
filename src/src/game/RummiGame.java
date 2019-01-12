package game;


import java.util.List;
import java.util.Map;

public class RummiGame implements Game {
  private RummiHand[] hands;
  private RummiTable table;
  private RummiBag bag;
  //CurrentHand only as int or as Object??
  private RummiHand currentHand;
  private int currentHandNumber;

  public RummiGame(int ages[]) {
    table = new RummiTable();
    hands = new RummiHand[ages.length];
    setCurrentHand(ages);
  }

  @Override
  public void moveStoneOnTable(Coordinate currentCoord, Coordinate nextCoord){

  }

  @Override
  public void moveStoneFromHand(Coordinate initialPosition, Coordinate targetPosition){
    Stone movedStone = currentHand.getStones().get(initialPosition);

    table.setStone(targetPosition, movedStone);
    currentHand.getStones().remove(initialPosition);
  }

  @Override
  public void moveStoneOnHand(Coordinate initialPosition, Coordinate targetPosition){
    Stone movedStone = currentHand.getStones().get(initialPosition);

    currentHand.setStone(targetPosition, movedStone);
    currentHand.getStones().remove(initialPosition);
  }

  @Override
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

  private void setCurrentHand(int[] ages) {
    currentHandNumber = 0;
    for (int i = 1; i < ages.length; i++) {
      if (ages[i] < ages[currentHandNumber]) {
        currentHandNumber = i;
      }
    }
  }

  @Override
  public void playerHasLeft() {

  }

  @Override
  public void undo() {

  }

  @Override
  public void nextTurn() {

  }

  @Override
  public boolean hasWinner() {
    return false;
  }

  @Override
  public boolean isConsistent() {
    return false;
  }

  @Override
  public Map<Coordinate, Stone> getTableStones() {
    return null;
  }

  @Override
  public Map<Coordinate, Stone> getCurrentHandStones() {
    return null;
  }

  @Override
  public List<Integer> getOtherHandSizes() {
    return null;
  }

  @Override
  public int getBagSize() {
    return 0;
  }

  @Override
  public int getCurrentHandNumber(){
    return 0;
  }
}

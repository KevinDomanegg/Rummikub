package game;


import java.util.List;
import java.util.Map;

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


  public RummiGame(int ages[]) {
    table = new RummiTable();
    hands = new RummiHand[ages.length];
    setCurrentHand(ages);
  }

  private void setCurrentHand(int[] ages) {
    currentHand = 0;
    for (int i = 1; i < ages.length; i++) {
      if (ages[i] < ages[currentHand]) {
        currentHand = i;
      }
    }
  }

  @Override
  public void start() {

  }

  @Override
  public void moveStoneonTable(Coordinate currentCoord, Coordinate nextCoord) {

  }

  @Override
  public void moveStoneFromHand(Coordinate currentCoord, Coordinate nextCoord) {

  }

  @Override
  public void moveStoneOnHand(Coordinate currentCoord, Coordinate nextCoord) {

  }

  @Override
  public void reset(int moves) {

  }

  @Override
  public void undo() {

  }

  @Override
  public boolean isConsistent() {
    return false;
  }

  @Override
  public void drawStone() {

  }

  @Override
  public void handDown() {

  }

  @Override
  public boolean hasWinnder() {
    return false;
  }

  @Override
  public Map<Coordinate, Stone> getTable() {
    return null;
  }

  @Override
  public Map<Coordinate, Stone> getCurrentHand() {
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
}

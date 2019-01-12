package game;


import java.util.List;
import java.util.Map;

public class RummiGame implements Game {
  private RummiTable table;
  private RummiHand[] hands;
  private int currentHand;
  // private Stack<MoveTrace>moveTraces;

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

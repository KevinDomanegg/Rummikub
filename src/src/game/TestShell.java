package game;

public class TestShell {

  public static void main (String[] args){
    testStone();
    testBag();
    testHand();
  }

  //Testmethod

  private static void testStone() {
    Stone stone = new Stone(Stone.Color.BLACK, 1);

    System.out.println(stone);
  }
  private static void testBag() {
    RummiBag bag = new RummiBag();
    RummiBag testBag = new RummiBag();

    //Test every Mehtod at least once.
    System.out.println(bag);
    bag.removeStone();
    System.out.println(bag);
    bag.addStones(testBag.getStones());
    System.out.println(bag.size());

    testBag = null;
    bag = null;
  }

  private static void testHand(){
    RummiHand hand = new RummiHand(25, "Cedrik", 1);
    Stone stone = new Stone(Stone.Color.JOKER, 0);
    Stone stone2 = new Stone(Stone.Color.RED, 13);
    Coordinate coordinate = new Coordinate(0,0);
    Coordinate coordinate2 = new Coordinate(1,1);

    hand.setStone(coordinate, stone);
    hand.setStone(coordinate2, stone2);

    System.out.println(hand);

    hand = null;
    stone = null;
  }







}

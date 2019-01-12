package game;

public class TestShell {

  public static void main (String[] args){
    testBag();
    testHand();

  }



  //Testmethod
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
    RummiHand hand = new RummiHand();
    Stone stone = new Stone(Stone.Color.JOKER, 0);
    Coordinate coordinate = new Coordinate(0,0);

    hand.setStone(coordinate, stone);

    System.out.println(hand);

    hand = null;
    stone = null;
  }







}

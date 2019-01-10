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
    System.out.println(bag.toString());
    bag.removeStone();
    System.out.println(bag.toString());
    bag.addStones(testBag.getStones());
    System.out.println(bag.size());

    testBag = null;
    bag = null;
  }

  private static void testHand(){

  }







}

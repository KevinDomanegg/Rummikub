package game;

public class TestShell {

  public static void main (String[] args){
    testStone();
    testBag();
    testHand();
    testGame();
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
    RummiHand hand = new RummiHand();
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

  public static void testGame(){
    RummiGame game = new RummiGame();
    game.setPlayer(17);
    game.setPlayer(19);
    game.start();
    System.out.println("Initial Game:");
    System.out.println(game.getPlayerStones(game.getCurrentPlayerID()));
    System.out.println(game.getTableStones());
    game.putStone(new Coordinate(0, 0), new Coordinate(0,0));
    System.out.println("One Stone was put donw");
    System.out.println(game.getPlayerStones(game.getCurrentPlayerID()));
    System.out.println(game.getTableStones());
    System.out.println(game.getTrace());
    game.undo();
    System.out.println(game.getTrace());
    System.out.println("Pick up Stone again");
    System.out.println(game.getPlayerStones(game.getCurrentPlayerID()));
    System.out.println(game.getTableStones());
    game.putStone(new Coordinate(0, 0), new Coordinate(0,0));
    game.putStone(new Coordinate(0, 1), new Coordinate(0,1));
    game.moveStoneOnTable(new Coordinate(0,0), new Coordinate(0,2));
    System.out.println("Moved Stone on Table");
    System.out.println(game.getPlayerStones(game.getCurrentPlayerID()));
    System.out.println(game.getTableStones());
    System.out.println(game.getTrace());
    game.undo();
    System.out.println("Undone that move");
    System.out.println(game.getPlayerStones(game.getCurrentPlayerID()));
    System.out.println(game.getTableStones());
    System.out.println(game.getTrace());
    game.undo();
    game.undo();
    System.out.println("Resetted");
    System.out.println(game.getPlayerStones(game.getCurrentPlayerID()));
    System.out.println(game.getTableStones());
    System.out.println(game.getTrace());
    game.moveStoneOnHand(game.getCurrentPlayerID(), new Coordinate(0, 0), new Coordinate(9, 0));
    System.out.println(game.getPlayerStones(game.getCurrentPlayerID()));
    game.undo();
    System.out.println(game.getPlayerStones(game.getCurrentPlayerID()));
  }







}

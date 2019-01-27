package game;

import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.Map;


public class RummiGameTest {

  @Test
  public void initTestWithoutMoveOnHand() {
    RummiGame game1 = new RummiGame();
    game1.setPlayer("Cedrik", 25);
    game1.setPlayer("Hyunsung", 21);
    game1.start();

    assertTrue(game1.getPlayerNames().get(1) == "Hyunsung");
    assertTrue(game1.getNumberOfPlayers() == 2);
    assertTrue((game1.getBagSize() + game1.getPlayerHandSizes().get(0) + game1.getPlayerHandSizes().get(1)) == 106);
    assertTrue(game1.getCurrentPlayerID() == 1);
    assertEquals(game1.getTableHeight(), 8);
    assertEquals(game1.getTableWidth(), 26);
    assertEquals(game1.getPlayerHandHeight(1), 3);
    assertEquals(game1.getPlayerHandWidth(1), 18);

    game1.putStone(new Coordinate(0, 0), new Coordinate(0, 0));
    game1.putStone(new Coordinate(1, 0), new Coordinate(1, 0));
    game1.putStone(new Coordinate(2, 0), new Coordinate(2, 0));

    game1.moveStoneOnTable(new Coordinate(0, 0), new Coordinate(3, 0));
    game1.moveStoneOnTable(new Coordinate(1, 0), new Coordinate(4, 0));

    assertTrue(game1.getTableStones().size() == 3);

    assertTrue(game1.getPlayerStones(1).get(new Coordinate(0, 0)) == null);


    assertEquals(game1.getTrace().peek().getCommand(), "MOVESTONEONTABLE");
    game1.undo();
    assertEquals(game1.getTrace().peek().getCommand(), ("MOVESTONEONTABLE"));
    game1.undo();
    game1.undo();
    assertEquals(game1.getTrace().peek().getCommand(), ("MOVESTONEFROMHAND"));
    game1.undo();
    game1.reset();
    game1.undo();
    assertTrue(game1.getTrace().empty());

    assertFalse(game1.hasWinner());
  }

  @Test
  public void consistencyTest() {
    RummiGame game2 = new RummiGame();
    game2.setPlayer("Cedrik", 25);
    game2.setPlayer("Hyunsung", 21);
    game2.start();

    game2.getTableStones().put(new Coordinate(0, 0), new Stone(Stone.Color.BLACK, 1));
    game2.getTableStones().put(new Coordinate(1, 0), new Stone(Stone.Color.BLACK, 2));
    game2.getTableStones().put(new Coordinate(2, 0), new Stone(Stone.Color.BLACK, 3));
    game2.getTableStones().put(new Coordinate(3, 0), new Stone(Stone.Color.BLACK, 4));

    assertTrue(game2.isConsistent());

    game2.getTableStones().put(new Coordinate(0, 1), new Stone(Stone.Color.BLACK, 7));
    game2.getTableStones().put(new Coordinate(1, 1), new Stone(Stone.Color.BLACK, 2));
    game2.getTableStones().put(new Coordinate(2, 1), new Stone(Stone.Color.BLACK, 3));
    game2.getTableStones().put(new Coordinate(3, 1), new Stone(Stone.Color.BLACK, 4));

    assertFalse(game2.isConsistent());
  }

  @Test
  public void playerHasLeftTest() {
    RummiGame game3 = new RummiGame();
    game3.setPlayer("Peter", 25);
    game3.setPlayer("Helga", 21);
    game3.setPlayer("Helga2", 20);
    game3.start();

    assertEquals(game3.getBagSize(), (106 - 42));
    game3.playerHasLeft(1);
    assertEquals(game3.getBagSize(), 106 - 28);
    game3.playerHasLeft(0);
    game3.playerHasLeft(0);

  }

  @Test
  public void rankingTest() {
    RummiGame game4 = new RummiGame();
    game4.setPlayer("Peter", 25);
    game4.setPlayer("Helga", 21);
    game4.start();

    game4.getPlayerStones(1).clear();

    assertTrue(game4.hasWinner());

    assertTrue(game4.getFinalRank().get(0).getValue() < 0);
    assertTrue(game4.getFinalRank().get(0).getKey() == 0);
    assertEquals(game4.getFinalRank().get(1).getValue(), 0.0, 0);
    assertTrue(game4.getFinalRank().get(1).getKey() == 1);
  }

  @Test
  public void sortHandByRunTest() {
    RummiGame game4 = new RummiGame();
    game4.setPlayer("Peter", 25);
    game4.setPlayer("Helga", 21);
    game4.start();

    boolean sortedByRun = true;
    game4.sortPlayerHandByRun(0);
    Map<Coordinate, Stone> testHand = game4.getPlayerStones(0);

    for (Map.Entry<Coordinate, Stone> entry : testHand.entrySet()) {
      Coordinate coord1 = entry.getKey();
      Stone.Color color = entry.getValue().getColor();
      int number = entry.getValue().getNumber();

      Coordinate coord2 = new Coordinate(coord1.getCol() + 1, coord1.getRow());

      if (testHand.get(coord2) != null) {
        if (testHand.get(coord2).getNumber() < number && testHand.get(coord2).getColor() == color) {
          sortedByRun = false;
        }

      }
    }

    assertTrue(sortedByRun);

  }

  @Test
  public void sortedHandByGroupTest() {

    RummiGame game4 = new RummiGame();
    game4.setPlayer("Peter", 25);
    game4.setPlayer("Helga", 21);
    game4.start();

    boolean sortedByGroup = true;
    game4.sortPlayerHandByGroup(0);
    Map<Coordinate, Stone> testHand = game4.getPlayerStones(0);

    for (Map.Entry<Coordinate, Stone> entry : testHand.entrySet()) {
      Coordinate coord1 = entry.getKey();
      Stone.Color color = entry.getValue().getColor();
      int number = entry.getValue().getNumber();

      Coordinate coord2 = new Coordinate(coord1.getCol() + 1, coord1.getRow());

      if (testHand.get(coord2) != null) {
        if (testHand.get(coord2).getNumber() < number) {
          sortedByGroup = false;
        }

      }
    }

    assertTrue(sortedByGroup);

  }

  @Test
  public void yetAnotherTest() {
    RummiGame game = new RummiGame();
    game.setPlayer("player1", 0);
    game.setPlayer("player2", 3);
    game.start();

    game.putStone(new Coordinate(4, 0), new Coordinate(3, 2));
    game.putStone(new Coordinate(5, 0), new Coordinate(4, 2));
    game.putStone(new Coordinate(6, 0), new Coordinate(5, 2));
    game.putStone(new Coordinate(7, 0), new Coordinate(6, 2));

    game.putStone(new Coordinate(0, 0), new Coordinate(3, 3));
    game.putStone(new Coordinate(1, 0), new Coordinate(4, 3));
    game.putStone(new Coordinate(2, 0), new Coordinate(5, 3));
    game.putStone(new Coordinate(3, 0), new Coordinate(6, 3));

    assertFalse(game.hasWinner());

    game.getTrace().clear();

    assertTrue(game.getTrace().empty());

    String firstTable = game.getTrace().toString();
    System.out.println("---first table");
    System.out.println(firstTable);
    game.moveSetOnTable(new Coordinate(6, 2), new Coordinate(0, 0));
    System.out.println("---moving set (6, 2) to (0, 0)");
    System.out.println(game.getTrace());
    game.moveSetOnTable(new Coordinate(3, 0), new Coordinate(10, 3));
    System.out.println("---moving set (3, 0) to (10, 3)");
    System.out.println(game.getTrace());
    game.moveSetOnTable(new Coordinate(8, 3), new Coordinate(4, 3));
    System.out.println("---moving set (8, 3) to (4, 3)");
    System.out.println(game.getTrace());
    game.moveStoneOnTable(new Coordinate(5, 3), new Coordinate(0, 0));
    System.out.println("---moving stone (5, 3) to (0, 0)");
    System.out.println(game.getTrace());
    game.moveSetOnTable(new Coordinate(3, 3), new Coordinate(20, 1));
    System.out.println("---moving set (3, 3) to (20, 1)");
    System.out.println(game.getTrace());

    game.reset();
    System.out.println("---reset");
    System.out.println(game.getTrace());
    System.out.println("---check if reset worked");
    System.out.println(game.getTrace().toString().equals(firstTable));
  }
}
package game;

import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;



public class RummiGameTest {

  @Test
  public void initTest() {
    RummiGame game1 = new RummiGame();
    game1.setPlayer("Cedrik", 25);
    game1.setPlayer("Hyunsung", 21);
    game1.start();

    assertTrue(game1.getPlayerNames().get(1) == "Hyunsung");
    assertTrue(game1.getNumberOfPlayers() == 2);
    assertTrue((game1.getBagSize() + game1.getPlayerHandSizes().get(0) + game1.getPlayerHandSizes().get(1)) == 106);
    assertTrue(game1.getCurrentPlayerID() == 1);
    assertTrue(game1.getTableHeight() == 5);
    assertTrue(game1.getTableWidth() == 20);
    assertTrue(game1.getPlayerHandHeight(1) == 2);
    assertTrue(game1.getPlayerHandWidth(1) == 20);

    game1.putStone(new Coordinate(0,0), new Coordinate(0,0));
    game1.putStone(new Coordinate(1,0), new Coordinate(1,0));
    game1.putStone(new Coordinate(2,0), new Coordinate(2,0));

    game1.moveStoneOnTable(new Coordinate(0,0), new Coordinate(3,0));
    game1.moveStoneOnTable(new Coordinate(1,0), new Coordinate(4,0));

    assertTrue(game1.getTableStones().size() == 3);

    game1.moveStoneOnHand(1, new Coordinate(3, 0), new Coordinate(0,0));

    assertTrue(game1.getPlayerStones(1).get(new Coordinate(0,0)) != null);


    assertTrue(game1.getTrace().peek().getCommand().equals("MOVESTONEONHAND"));
    game1.undo();
    assertTrue(game1.getTrace().peek().getCommand().equals("MOVESTONEONTABLE"));
    game1.undo();
    game1.undo();
    assertTrue(game1.getTrace().peek().getCommand().equals("MOVESTONEFROMHAND"));
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

    game2.getTableStones().put(new Coordinate(0,0), new Stone(Stone.Color.BLACK, 1));
    game2.getTableStones().put(new Coordinate(1,0), new Stone(Stone.Color.BLACK, 2));
    game2.getTableStones().put(new Coordinate(2,0), new Stone(Stone.Color.BLACK, 3));
    game2.getTableStones().put(new Coordinate(3,0), new Stone(Stone.Color.BLACK, 4));

    assertTrue(game2.isConsistent());

    game2.getTableStones().put(new Coordinate(0,1), new Stone(Stone.Color.BLACK, 7));
    game2.getTableStones().put(new Coordinate(1,1), new Stone(Stone.Color.BLACK, 2));
    game2.getTableStones().put(new Coordinate(2,1), new Stone(Stone.Color.BLACK, 3));
    game2.getTableStones().put(new Coordinate(3,1), new Stone(Stone.Color.BLACK, 4));

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
}
package game;

import org.junit.Test;

import static org.junit.Assert.*;

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
}
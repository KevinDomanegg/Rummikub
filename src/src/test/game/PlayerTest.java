package game;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


public class PlayerTest {

  @Test
  public void initTest(){
    Player player1 = new Player("Hyunsung", 23);

    assertTrue(player1.getAge() == 23);
    assertTrue(player1.getName() == "Hyunsung");
    assertTrue(player1.getHandHeight() == 2);
    assertTrue(player1.getHandWidth() == 20);

    player1.pushStone(new Stone(Stone.Color.RED, 1));
    player1.pushStone(new Stone(Stone.Color.YELLOW, 2));
    player1.pushStone(new Stone(Stone.Color.JOKER, 3));

    //A Joker counts as 20 negative points, extra rule!
    assertTrue(player1.getPoints() == -23 || player1.getPoints() == -6);

    assertTrue(player1.getStones().size() == 3);
  }

  @Test
  public void moveTest(){
    Player player2 = new Player("Cedrik", 22);

    player2.pushStone(new Stone(Stone.Color.YELLOW, 2));
    player2.pushStone(new Stone(Stone.Color.JOKER, 3));

    player2.moveStone(new Coordinate(0,0), new Coordinate(3,0));
    Stone stone1 = player2.getStones().get(new Coordinate(3,0));

    assertTrue(stone1.getColor() == Stone.Color.YELLOW);
    assertTrue(stone1.getNumber() == 2);

  }

  @Test
  public void fillHandTest(){
    Player player3 = new Player("Ella", 19);

    for (int i = 0; i < player3.getHand().getWidth(); i++){
      for (int j = 0; j < player3.getHand().getHeight(); j++){
        player3.getHand().setStone(new Coordinate(i,j), new Stone(Stone.Color.JOKER, 1));
      }
    }

    player3.pushStone(new Stone(Stone.Color.RED, 1));
    player3.pushStone(new Stone(Stone.Color.RED, 2));
    assertTrue(player3.getHandSize() == player3.getHandHeight() * player3.getHandWidth());
    player3.popStone(new Coordinate(0,0));
    assertTrue(player3.getHandSize() == player3.getHandHeight() * player3.getHandWidth() - 1);

    player3.playedFirstMove();

    assertTrue(player3.hasPlayedFirstMove() == true);

  }
}
package network.server;

//import game.ConcreteGame;
import game.Game;
import game.RummiGame;

/**
 * Class that builds an initializes a Game-Object.
 */
public class GameBuilder {

  private Game game;

  GameBuilder(RummiServer server) {
    this.game = new RummiGame();
  }

  Game getGame() {
    return this.game;
  }
}

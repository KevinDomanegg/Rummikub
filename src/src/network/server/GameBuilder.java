package network.server;

import game.ConcreteGame;
import game.Game;

/**
 * Class that builds an initializes a Game-Object.
 */
public class GameBuilder {

  private Game game;

  GameBuilder(RummiServer server) {
    this.game = new ConcreteGame(server);
  }

  Game getGame() {
    return this.game;
  }
}

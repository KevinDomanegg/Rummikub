package communication.gameinfo;

public interface GameInfo {
  /**
   * Interface defining classes that contain information about changes in the game.
   * Used to communicate such chances from the server to the clients.
   * Implementations of this interface should be specific to a specific type of change,
   * e.g. movement of a stone
   * Each implementation must have a unique GameInfoID.
   */

  /**
   * @return GameInfoID to identify the concrete implementation of the interface.
   */
  public GameInfoID getGameInfoID();

}

package network.server;

import communication.gameinfo.GameInfo;

public interface Server {

  /**
   * Sends a GameInfo to all clients.
   * @param info GameInfo to be sent
   */
  public void sendToAll(GameInfo info);

  /**
   * Sends a GameInfo to the player that is currently playing.
   *
   * @param info GameInfo getting sent to the player
   * @param playerid id of the current player (0-n)
   */
  public void sendToCurrentPlayer(GameInfo info, int playerid);
}

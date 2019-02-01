package network.server;

import communication.gameinfo.*;
import communication.request.UpdatePlayersRequest;
import game.Game;
import game.RummiGame;
import network.client.RummiClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class RummiServer extends Thread implements Server {


  private static final int MAX_CLIENTS = 4;
  private static final int PORT = 48410;
  private static Socket[] clients = new Socket[MAX_CLIENTS];
  private static ServerListener[] listeners = new ServerListener[MAX_CLIENTS];
  private static ServerSender[] senders = new ServerSender[MAX_CLIENTS];
  private ServerSocket server;
  private int numOfClients;
  private boolean running = true;
  private RequestHandler requestHandler;
  private Game game;

  /**
   * Constructor creating a new Server, including all other classes needed
   * server-side to play the game.
   */
  public RummiServer() {
    game = new RummiGame();
    requestHandler = new RequestHandler(this, game);
  }

  /**
   * Starts the server.
   * Connects to all the clients and initializes Listeners and Senders.
   */
  @Override
  public void run() {
    try {
      server = new ServerSocket(PORT);
      Socket client;

      while (running) {
        synchronized (this) {
          client = server.accept();
          tryToConnect(client);
        }
      }
    } catch (IOException e) {
      this.running = false;
    }
  }

  private synchronized void tryToConnect(Socket client) {

    if (numOfClients >= MAX_CLIENTS) {
      rejectClient(client);
      return;
    }

    // find next free position of clients
    for (int i = 0; i < MAX_CLIENTS; i++) {
      if (clients[i] == null) {
        connectClient(client, i);
        break;
      }
    }

  }


  /**
   * Notifies a client that it has been rejected.
   * @param client to be rejected
   */
  private void rejectClient(Socket client) {
    ServerSender sender = new ServerSender(client, this, (MAX_CLIENTS + 1));
    sender.start();
    sender.send(new SimpleGameInfo(GameInfoID.TOO_MANY_CLIENTS));
    sender.disconnect();
  }

  /**
   * Connects to a certain client.
   *
   * @param client that will be connected
   * @param id     of the client
   *               position of the clients among the other clients
   */
  private void connectClient(Socket client, int id) {
    clients[id] = client;
    listeners[id] = new ServerListener(clients[id], this, id);
    listeners[id].start();
    senders[id] = new ServerSender(clients[id], this, id);
    senders[id].start();
    numOfClients++;

    // SENDS THE IP ADDRESS OF THE SERVER
    try {
      sendToPlayer(id, new GameIPAddress(getIP()));
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  /**
   * Disconnects from a certain client.
   *
   * @param id of the client
   */
  void disconnectClient(int id) {

    //Remove player and notify clients about it
    game.removePlayer(id);
    requestHandler.applyRequest(new UpdatePlayersRequest(), id);

    clients[id] = null;
    listeners[id].disconnect();
    listeners[id] = null;
    senders[id].disconnect();
    senders[id] = null;
    this.numOfClients--;

    //Notify all clients if the host is the one disconnecting.
    if (id == 0) {
      sendToAll(new SimpleGameInfo(GameInfoID.SERVER_NOT_AVAILABLE));
    }

    if (noClientConnected()) {
      suicide();
    }
  }

  private boolean noClientConnected() {
    boolean noClient = true;

    for (Socket client : clients) {
      if (client != null) {
        noClient = false;
      }
    }

    return noClient;
  }

  /**
   * Applies the request to the Game.
   *
   * @param request to be applied
   */
  void applyRequest(Object request, int socketID) {
    requestHandler.applyRequest(request, socketID);
  }

  /**
   * Sends a GameInfo to all clients.
   *
   * @param info GameInfo to be sent
   */
  public void sendToAll(GameInfo info) {
    for (ServerSender sender : senders) {
      if (sender != null) {
        sender.send(info);
      }
    }
  }

  /**
   * Sends a GameInfo to a player specified player.
   *
   * @param playerID id of the player (0-n)
   * @param info     GameInfo getting sent to the player
   */
  @Override public void sendToPlayer(int playerID, GameInfo info) {
    if (senders[playerID] != null) {
      senders[playerID].send(info);
    }
  }


  /**
   * Returns the IP-address of the server.
   *
   * @return String representing the IP-address
   * @throws UnknownHostException whenever the IP-address could not be determined
   */
  @Override
  public String getIP() throws UnknownHostException{
    return InetAddress.getLocalHost().getHostAddress();
  }

  /**
   * Stops the thread and closes the closables.
   */
  private void suicide() {

    running = false;
    try {
      server.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

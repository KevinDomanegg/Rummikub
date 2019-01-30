package network.server;

import communication.gameinfo.GameIPAddress;
import communication.gameinfo.GameInfo;
import communication.gameinfo.GameInfoID;
import communication.gameinfo.SimpleGameInfo;
import game.Game;
import game.RummiGame;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class RummiServer extends Thread implements Server {


  public static void main(String[] args) {
//    RummiServer s = new RummiServer();
//    s.start();
  }


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

          if (numOfClients >= MAX_CLIENTS) {
            try {
              wait();
            } catch (InterruptedException e) {
              running = false;
            }
          }
          // find next free position of clients
          client = server.accept();
          for (int i = 0; i < MAX_CLIENTS; i++) {
            if (clients[i] == null) {
              connectClient(client, i);
              numOfClients++;
              break;
            }
          }
          // for test
          System.out.println("number of clients: " + numOfClients);
        }
      }
      //cleanup();
    } catch (IOException e) {
      this.running = false;
    }
    System.out.println("Server terminated");
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
    System.out.println("RummiServer: connected to " + id);
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

    System.out.println("-----client disconnected: "+ id);
    System.out.println("-----player numbers: "+ game.getNumberOfPlayers());
    game.kickPlayer(id);
    clients[id] = null;
    if (listeners[id] != null) {
      listeners[id].disconnect();
    }
    listeners[id] = null;
    if (senders[id] !=  null) {
      senders[id].disconnect();
    }
    senders[id] = null;
    this.numOfClients--;
    System.out.println("RummiServer: disconnected from " + id);

    //When the client who has hosted the game disconnects, terminate the server
    if (id == 0) {
      sendToAll(new SimpleGameInfo(GameInfoID.SERVER_NOT_AVAILABLE));
      suicide();
    }

    //notifyAll();
  }

  private void cleanup() {
    for (int i = 0; i < clients.length; i++) {
      if (clients[i] != null) {
        disconnectClient(i);
      }
    }
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

  public void suicide() {
    cleanup();
    running = false;
    try {
      server.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

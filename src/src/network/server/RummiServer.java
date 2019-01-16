package network.server;

import communication.gameinfo.GameInfo;
import communication.gameinfo.HandInfo;
import communication.gameinfo.TableInfo;
import communication.request.Request;

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
      ServerSocket server = new ServerSocket(PORT);
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
    } catch (IOException e) {
      this.running = false;
    }
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
  }

  /**
   * Disconnects from a certain client.
   *
   * @param id of the client
   */
  synchronized void disconnectClient(int id) {
    clients[id] = null;
    listeners[id].disconnect();
    listeners[id] = null;
    senders[id].disconnect();
    senders[id] = null;
    this.numOfClients--;
    System.out.println("RummiServer: disconnected from " + id);
    notifyAll();
  }

  /**
   * Applies the request to the Game.
   *
   * @param request to be applied
   */
  void applyRequest(Request request, int socketID) {
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
   * Sends a GameInfo to the player that is currently playing.
   *
   * @param playerID id of the current player (0-n)
   * @param info     GameInfo getting sent to the player
   */
  @Override public void sendToPlayer(int playerID, GameInfo info) {
    senders[playerID].send(info);
  }


  /**
   * Returns the IP-address of the server.
   *
   * @return String representing the IP-address
   * @throws UnknownHostException whenever the IP-address could not be determined
   */
  public String getIP() throws UnknownHostException{
    return InetAddress.getLocalHost().getHostAddress();
  }

}

package network.server;

import communication.GameInfo;
import communication.Request;

import communication.RequestHandler;
import game.ConcreteGame;
import game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RummiServer extends Thread {


  public static void main(String[] args) {
    ConcreteGame game = new ConcreteGame();
    RummiServer s = new RummiServer(game);
    game.setserver(s);
    s.start();
  }


  private static final int MAX_CLIENTS = 4;
  private static Socket[] clients = new Socket[MAX_CLIENTS];
  private static ServerListener[] listeners = new ServerListener[MAX_CLIENTS];
  private static ServerSender[] senders = new ServerSender[MAX_CLIENTS];
  private int numOfClients = 0;
  private boolean running = true;
  private RequestHandler reqhandler;
  private Game game;

  public RummiServer(Game game){
    this.game = game;
    this.reqhandler = new RequestHandler(game);
  }

  @Override
  public void run() {
    try {
      ServerSocket server = new ServerSocket(3141);

      while (running) {

        synchronized (this) {
          if (numOfClients >= MAX_CLIENTS) {
            try {
              wait();
            } catch (InterruptedException e) {
              running = false;
            }
          }
        }

        Socket client = server.accept();
        for (int i = 0; i < MAX_CLIENTS; i++) {
          if (clients[i] == null) {
            connectClient(client, i);
            break;
          }
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
   * @param id of the client
   *           position of the clients among the other clients
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
   * @param request to be applied
   */
  void applyRequest(Request request) {
    reqhandler.applyRequest(request);
  }

  /**
   * Sends a GameInfo to all clients.
   * @param info GameInfo to be sent
   */
  public void sendToAll(GameInfo info){
    for (ServerSender sender : senders) {
      sender.send(info);
    }
  }

  /**
   * Sends a GameInfo to the player that is currently playing.
   *
   * @param info GameInfo getting sent to the player
   * @param playerid id of the current player (0-n)
   */
  public void sendToCurrentPlayer(GameInfo info, int playerid) {
    senders[playerid].send(info);
  }

}

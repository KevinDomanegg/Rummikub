package network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerListener extends Thread {
  private RummiServer server;
  private Socket clientIn;
  private int id;
  private boolean connected = false;
  ObjectInputStream in;

  /**
   * Constructor setting the necessary instance variables.
   *
   * @param clientIn client the listener listens to
   * @param server   the listener corresponds to
   * @param id       of the listener, assigned by the server
   */
  ServerListener(Socket clientIn, RummiServer server, int id) {
    this.clientIn = clientIn;
    this.server = server;
    this.id = id;
  }

  /**
   * Listens for Requests from the specified client and applies them to the server.
   */
  @Override
  public void run() {

    try {
      in = new ObjectInputStream(clientIn.getInputStream());
    } catch (IOException e) {
      return;
    }
    this.connected = true;

    Object request = null;
    while (connected) {
      try {
        request = in.readObject();
      } catch (IOException e) {
        connected = false;
        server.disconnectClient(id);
        System.out.println("Client " + id + " has disconnected");
        return;
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
      System.out.println("Listener: Received " + request);
      server.applyRequest(request, id);
    }
    System.out.println("ServerListener terminated");
  }

  void disconnect() {
    this.connected = false;
    try {
      this.clientIn.close();
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

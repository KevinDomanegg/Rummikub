package network.server;

import communication.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerListener extends Thread {
  private RummiServer server;
  private Socket clientIn;
  private int id;
  private boolean connected = false;
  private Request request;

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

      ObjectInputStream in = new ObjectInputStream(clientIn.getInputStream());
      this.connected = true;

      while (connected) {
        Object o = null;
        try {
          o = in.readObject();
          request = (Request) o;
        } catch (ClassNotFoundException | ClassCastException e) {
          connected = false;
        }

        if (o == null) {
          System.out.println("Client " + id + " not connected");
          this.connected = false;
          server.disconnectClient(id);
          break;
        }

        System.out.println("Listener: Received " + request.toString());
        server.applyRequest(request);

      }
    } catch (IOException e) {
      this.connected = false;
      server.disconnectClient(id);
    }
  }

  void disconnect() {
    this.connected = false;
  }
}

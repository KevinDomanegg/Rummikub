package network.server;

import communication.request.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerListener extends Thread {
  private RummiServer server;
  private Socket clientIn;
  private int id;
  private boolean connected = false;
  private Request request;
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
      this.connected = true;

      while (connected) {
        Object o = null;
        try {
          o = in.readObject();
          request = (Request) o;
        } catch (ClassNotFoundException | ClassCastException e) {
          connected = false;
        } catch (IOException e) {
          connected = false;
          server.disconnectClient(id);
          System.out.println("Client " + id + " has disconnected");
        }

        if (o == null) {
          System.out.println("Client " + id + " not connected");
          System.out.println("ServerListener just got a NULL value");
          this.connected = false;
          server.disconnectClient(id);
          break;
        }

        System.out.println("Listener: Received " + request.toString());
        server.applyRequest(request, id);

      }
    } catch (IOException e) {
      this.connected = false;
      server.disconnectClient(id);
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

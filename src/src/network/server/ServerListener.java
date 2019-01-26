package network.server;

import communication.Deserializer;
import communication.request.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerListener extends Thread {
  private RummiServer server;
  private Socket clientIn;
  private int id;
  private boolean connected = false;
  private Request request;
  private Scanner in;
  private Deserializer deserializer;

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

      deserializer = new Deserializer();
      in = new Scanner(clientIn.getInputStream());
      this.connected = true;

      while (connected) {
        String json = null;
        try {
          json = in.nextLine();
          request = deserializer.deserializeRequest(json);
        } catch (ClassCastException  | NoSuchElementException e) {
          disconnect();
        }

        if (json == null) {
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

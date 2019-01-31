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
  private boolean connected;
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
      in = new Scanner(clientIn.getInputStream());
    } catch (IOException e) {
      return;
    }
    deserializer = new Deserializer();
    String json;
    // While Loop is going to break if the client on the other side
    // ends the connection with the Server
    while (true) {
      try {
        json = in.nextLine();
      } catch (NoSuchElementException e) {
        break;
      }
      request = deserializer.deserializeRequest(json);
      System.out.println("Listener: Received " + request.toString());
      server.applyRequest(request, id);
    }
    server.disconnectClient(id);
    System.out.println("ServerListener terminated");
  }

  void disconnect() {
    try {
      this.clientIn.close();
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

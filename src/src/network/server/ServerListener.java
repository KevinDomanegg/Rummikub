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
    deserializer = new Deserializer();
    this.connected = true;
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

    while (connected) {
      connected = processMessages();
    }

    server.disconnectClient(id);
  }

  /**
   * Receives messages over the network, deserializes and applies them.
   *
   * @return boolean indicating if the Listener is still connected.
   */
  private boolean processMessages() {
    String json;
    try {
      json = in.nextLine();
    } catch (NoSuchElementException e) {
      return false;
    }
    request = deserializer.deserializeRequest(json);
    server.applyRequest(request, id);
    return true;
  }

  /**
   * Closes all Closables.
   */
  void disconnect() {
    try {
      this.clientIn.close();
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

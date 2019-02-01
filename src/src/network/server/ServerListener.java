package network.server;

import communication.Deserializer;
import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerListener extends Thread {
  private RummiServer server;
  private Socket clientIn;
  private int id;
  private boolean connected;
  private Scanner in;

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
    Deserializer deserializer = new Deserializer();
    try {
      while (connected) {
        server.applyRequest(deserializer.deserializeRequest(in.nextLine()), id);
      }
    } catch (NoSuchElementException e) {
      if (connected) {
        System.out.println("From Run in ServerListener: server.disconnectClient");
        server.disconnectClient(id);
      }
    }
    System.out.println("ServerListener terminated");
  }

  void disconnect() {
    System.out.println("From ServerListener: disconnecting...");
    connected = false;
    try {
      this.clientIn.close();
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

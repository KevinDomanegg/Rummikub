package network.server;

import communication.gameinfo.GameInfo;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerSender extends Thread {
  private Socket clientOut;
  private RummiServer server;
  private int id;
  private boolean send = false;
  private boolean connected = true;
  private GameInfo info = null;

  /**
   * Constructor setting the necessary instance variables.
   *
   * @param clientOut client the Sender sends to
   * @param server corresponding to the Sender
   * @param id fo the Sender, assigned by the server
   */
  ServerSender(Socket clientOut, RummiServer server, int id) {
    this.clientOut = clientOut;
    this.server = server;
    this.id = id;
  }

  /**
   * Notifies the run()-Method to send the info.
   *
   * @param info to be sent
   */
  synchronized void send(GameInfo info) {
    this.info = info;
    send = true;
    notifyAll();
  }

  /**
   * Returns the socket the sender sends to.
   *
   * @return Socket the Sender sends to
   */
  Socket getClient() {
    return clientOut;
  }

  /**
   * Waits for the send-method being invoked and then sends the GameInfo.
   */
  @Override
  public void run() {

    synchronized (this) {
      try {
        while (connected) {
          if (send) {
            ObjectOutputStream out = new ObjectOutputStream(clientOut.getOutputStream());
            out.writeObject(this.info);
            out.flush();
          }
          this.send = false;
          wait();
        }
      } catch (Exception e) {
        this.connected = false;
        server.disconnectClient(this.id);
      }
    }
  }

  /**
   * Disconnects from the client.
   */
  void disconnect(){
    this.connected = false;
  }
}

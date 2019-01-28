package network.server;

import communication.Serializer;
import communication.gameinfo.GameInfo;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerSender extends Thread {
  private Socket clientOut;
  private RummiServer server;
  private int id;
  private boolean send = false;
  private boolean connected = true;
  private GameInfo info = null;
  private Serializer serializer;

  private PrintWriter out;

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
    this.serializer = new Serializer();
    try {
      this.out = new PrintWriter(clientOut.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Notifies the run()-Method to send the info.
   *
   * @param info to be sent
   */
  synchronized void send(GameInfo info) {
    /*this.info = info;
    send = true;
    notifyAll();*/
      String json = serializer.serialize(info);
      out.println(json);
      out.flush();

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
        //ObjectOutputStream out = new ObjectOutputStream(clientOut.getOutputStream());
        while (connected) {
          // IF WE DO SO, THEN WE HAVE A RACING CONDITION PROBLEM WITH SEND AND INFO, WHEN WE TRY TO SEND
          // A LOT OF DIFFERENT OBJECTS AT ONE TIME
          /*if (send) {
            out.writeObject(this.info);
            out.flush();
          }*/
          //this.send = false;
          wait();
        }
      } catch (Exception e) {
        this.connected = false;
//        server.disconnectClient(this.id);
      }
    }
    System.out.println("ServerSender terminated");
  }

    /**
     * Disconnects from the client.
     */
  synchronized void disconnect(){
    this.connected = false;
    try{
      this.clientOut.close();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    notifyAll();
  }
}

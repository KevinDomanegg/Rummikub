package network.client;

import communication.Deserializer;
import communication.gameinfo.GameInfo;
import communication.gameinfo.GameInfoID;
import communication.gameinfo.SimpleGameInfo;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**ClientListener listens to the Server
 * and when the Server sends an object to the Client
 * the Client Listener receives it, decrypts it and
 * forwards it to RummiClient
 */
class ClientListener extends Thread {
  //THE CLIENT THAT THE LISTENER LISTENS FOR..
  private Socket server;
  private RummiClient myClient;
  private boolean connected;
  private Scanner receiveMessage;
  private Deserializer deserializer;

  /**Creates a Listener that listens to a certain port
   * and communicates with only one client
   * @param server the port that listens to
   * @param myClient the client that reports the receiving objects to
   */
  ClientListener(Socket server, RummiClient myClient) {
    this.server = server;
    this.myClient = myClient;
    connected = true;
    deserializer = new Deserializer();
  }

  @Override
  public void run() {
    try {
      receiveMessage = new Scanner(server.getInputStream());
    } catch (IOException e) {
      myClient.disconnect();
    }
    String json;
    try {
      while (connected) {
        json = receiveMessage.nextLine();
        if (json != null) {
          GameInfo info = deserializer.deserializeInfo(json);
          if (info != null) {
            myClient.applyGameInfoHandler(info);
          }
        }
      }
    } catch (NoSuchElementException e) {
      if (connected) {
        myClient.notifyServerCLose();
      }
    }
  }

  void disconnect() {
    System.out.println("Called disconnect in ClientListener");
    connected = false;
    try {
      server.close();
      receiveMessage.close();
    } catch (IOException e) {
      System.out.println("exception while closing the listener");
      e.printStackTrace();
    }
  }
}

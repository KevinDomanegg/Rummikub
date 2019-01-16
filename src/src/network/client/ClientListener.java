package network.client;

import communication.gameinfo.GameInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Stack;


class ClientListener extends Thread {

  //THE CLIENT THAT THE LISTENER LISTENS FOR..
  private Socket server;
  private RummiClient myClient;
  private Stack<GameInfo> infos = new Stack<>();
  private boolean connected;

  //CREATES A LISTENER FOR ONLY ONE CLIENT
  ClientListener(Socket server, RummiClient myClient) {
    this.server = server;
    this.myClient = myClient;
    connected = true;
  }

  @Override
  public void run() {
    try {
      ObjectInputStream recieveMessage = new ObjectInputStream(server.getInputStream());
      while (connected) {
        try {
          System.out.println("Client listener is waiting for a message...");
          this.infos.push((GameInfo) recieveMessage.readObject());

          while (recieveMessage.available() > 0) {
            this.infos.push((GameInfo) recieveMessage.readObject());
          }
          applyInfos();
        } catch (ClassNotFoundException e) {
          connected = false;
        }
      }
    } catch (IOException e) {
      connected = false;
    }
  }

  /**
   * Applies all saved GameInfo's to the GamrInfoHandler
   */
  private void applyInfos() {
    GameInfo info;
    while (!infos.empty()) {
      info = infos.pop();
      myClient.applyGameInfoHandler(infos.pop());
      System.out.println("Client Listener applied this gameinfo " + info);
    }

  }


}

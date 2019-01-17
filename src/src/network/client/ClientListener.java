package network.client;

import communication.gameinfo.GameInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


class ClientListener extends Thread {

  //THE CLIENT THAT THE LISTENER LISTENS FOR..
  private Socket server;
  private RummiClient myClient;
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
          GameInfo gameinfo = (GameInfo) recieveMessage.readObject();
          myClient.applyGameInfoHandler(gameinfo);
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          connected = false;
        }
      }

    } catch (IOException e) {
      connected = false;
    }
  }

}

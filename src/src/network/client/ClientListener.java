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
  private ObjectInputStream receiveMessage;

  //CREATES A LISTENER FOR ONLY ONE CLIENT
  ClientListener(Socket server, RummiClient myClient) {
    this.server = server;
    this.myClient = myClient;
    connected = true;
  }

  @Override
  public void run() {
    try {
      receiveMessage = new ObjectInputStream(server.getInputStream());

      while (connected) {
        Object o;
        try {

          o = receiveMessage.readObject();
          if (o instanceof GameInfo) {

            myClient.applyGameInfoHandler(o);
          }
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
          disconnect();
        } catch (IOException e) {
          disconnect();
        }
      }

    } catch (IOException e) {
      disconnect();
    }

  }

  void disconnect() {
    System.out.println("Called disconnect in ClientListener");
    connected = false;
    try {
      receiveMessage.close();
      server.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

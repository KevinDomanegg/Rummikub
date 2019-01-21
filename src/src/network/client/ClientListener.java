package network.client;

import communication.Deserializer;
import communication.gameinfo.GameInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;


class ClientListener extends Thread {

  //THE CLIENT THAT THE LISTENER LISTENS FOR..
  private Socket server;
  private RummiClient myClient;
  private boolean connected;
  private Scanner receiveMessage;
  private Deserializer deserializer;

  //CREATES A LISTENER FOR ONLY ONE CLIENT
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

      while (connected) {
        String json;
          json = receiveMessage.nextLine();
          GameInfo info = deserializer.deserializeInfo(json);
          if (info instanceof GameInfo) {

            myClient.applyGameInfoHandler(info);
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

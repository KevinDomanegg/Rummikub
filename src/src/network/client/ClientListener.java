package network.client;

import communication.Deserializer;
import communication.gameinfo.GameInfo;
import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
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
    } catch (IOException e) {
      myClient.disconnect();
    }
      while (connected) {
        String json = null;
        try {
          json = receiveMessage.nextLine();
        } catch (NoSuchElementException e) {
          myClient.disconnect();
        }
        if (json != null) {
          GameInfo info = deserializer.deserializeInfo(json);
          if (info != null) {
            myClient.applyGameInfoHandler(info);
          }
        }
      }

  }

   void disconnect() {
    System.out.println("Called disconnect in ClientListener");
    connected = false;
    try {
      server.close();
      receiveMessage.close();
      //server.close();
    } catch (IOException e) {
      System.out.println("exception while closing the listener");
      e.printStackTrace();
    }
  }

}

package network.client;

import communication.gameinfo.GameInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


public class ClientListener extends Thread {

  //THE CLIENT THAT THE LISTENER LISTENS FOR..
  Socket server;
  RummiClient myClient;

  //CREATES A LISTENER FOR ONLY ONE CLIENT
  public ClientListener(Socket server, RummiClient myClient) {
    this.server = server;
    this.myClient = myClient;
  }

  @Override
  public void run() {
    try {
      ObjectInputStream recieveMessage = new ObjectInputStream(server.getInputStream());
      while (true) {
        try {
          Object game_info = recieveMessage.readObject();
              GameInfo recieveFromServer = (GameInfo) game_info;
              System.out.println("Client Listener got this gameinfo " + recieveFromServer); // TO MAKE SURE THAT THE MESSAGE WAS RECEIVED
              this.myClient.setForwardToController(recieveFromServer);
        } catch (ClassNotFoundException e) {}
      }
    } catch (IOException e) {}
  }


}

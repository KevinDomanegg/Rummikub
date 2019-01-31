package network.client;

import communication.Serializer;
import communication.request.Request;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class RummiClient extends Thread {

  //Connection variables
  private boolean connected;
  private Socket serverSocket;
  private PrintWriter outToServer;
  ClientListener listener;
  private boolean serverOK = true;
  private Serializer serializer;

  //GameInfoHandler
  private GameInfoHandler gameInfoHandler;


  //CREATE A NEW CLIENT WITH USERNAME, AGE AND IP ADDRESS OF THE SERVER("localhost" or ip)
  public RummiClient(String serverIPAddress) {
    connected = true;
    serializer = new Serializer();
    try {
      serverSocket = new Socket(serverIPAddress, 48410);
      outToServer = new PrintWriter(serverSocket.getOutputStream());
    } catch (IOException e) {
      System.out.println("There is no server in this ip address!");
      serverOK = false;
    }
  }

  public boolean isServerOK() {
    return serverOK;
  }

  public void setGameInfoHandler(GameInfoHandler gameInfoHandler) {
    this.gameInfoHandler = gameInfoHandler;
  }

  @Override
  public void run() {
      //serverSocket = new Socket(serverIPAddress, 48410);
      // Add a listener to this Client
      listener = new ClientListener(serverSocket, this);
      listener.start();
      synchronized (this) {
        try {
        //As long as the Client is connected to the Server
        while (connected) {
          try {
            wait();
          } catch (InterruptedException e) {
            disconnect();
          }
        }
        //NOT CONNECTED ANYMORE
        outToServer.close();
        serverSocket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      System.out.println("Client terminated");
  }

  void applyGameInfoHandler(Object gameInfo) {
    gameInfoHandler.applyGameInfo(gameInfo);
  }

  public synchronized void sendRequest(Object request) {

      String json = serializer.serialize((Request) request);
      outToServer.println(json);
      outToServer.flush();
  }

  public synchronized void disconnect() {
    listener.disconnect();
    this.connected = false;
    notifyAll();
  }
}

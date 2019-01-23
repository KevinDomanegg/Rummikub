package network.client;

import communication.gameinfo.GameInfoID;
import communication.gameinfo.SimpleGameInfo;

import java.io.*;
import java.net.Socket;


public class RummiClient extends Thread {

  //Connection variables
  private boolean connected;
  private Socket serverSocket;
  private ObjectOutputStream outToServer;
  ClientListener listener;
  private boolean serverOK = true;

  //GameInfoHandler
  private GameInfoHandler gameInfoHandler;

  // for shell demo
  private GameInfoHandler_Shell gameInfoHandler_shell;


  //CREATE A NEW CLIENT WITH USERNAME, AGE AND IP ADDRESS OF THE SERVER("localhost" or ip)
  public RummiClient(String serverIPAddress) {
    connected = true;
    try {
      serverSocket = new Socket(serverIPAddress, 48410);
      outToServer = new ObjectOutputStream(serverSocket.getOutputStream());
    } catch (IOException e) {
      System.out.println("There is no server in this ip address!");
      serverOK = false;
    }
  }

  public boolean isServerOK() {
    return serverOK;
  }

  public void setGameInfoHandler_Shell(GameInfoHandler_Shell gameInfoHandler) {
    this.gameInfoHandler_shell = gameInfoHandler;
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
    try {
      //out = new ObjectOutputStream(clientOut.getOutputStream());
      outToServer.writeObject(request);
      outToServer.flush();
    } catch (IOException e) {
      connected = false;
      notifyAll();
    }
  }

  public synchronized void disconnect() {
    listener.disconnect();
    this.connected = false;
    notifyAll();
  }
}

package network.client;

import communication.gameinfo.GameInfo;
import communication.request.ConcreteSetPlayer;
import communication.request.Request;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class RummiClient extends Thread {

  //Connection variables
  private boolean connected;
  private Socket server;
  private String serverIPAddress;
  private ObjectOutputStream outToServer;


  //Player ID
  private String username;
  private Integer age;

  //GameInfoHandler
  private GameInfoHandler gameInfoHandler;

  //SEND AND RECEIVE VARIABLES
  private GameInfo forwardToController;
  private Request request;
  private boolean readyToSend;


  //CREATE A NEW CLIENT WITH USERNAME, AGE AND IP ADDRESS OF THE SERVER("localhost" or ip)
  public RummiClient(String username, Integer age, String serverIPAddress) {
    this.username = username;
    this.age = age;
    this.serverIPAddress = serverIPAddress;
    connected = true;
    request = new ConcreteSetPlayer(age);
    readyToSend = true;
    try {
      this.server = new Socket(serverIPAddress, 48410);
      this.outToServer = new ObjectOutputStream(server.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setGameInfoHandler(GameInfoHandler gameInfoHandler) {
    this.gameInfoHandler = gameInfoHandler;
  }

  @Override
  public void run() {
      //server = new Socket(serverIPAddress, 48410);
      // Add a listener to this Client
      ClientListener listener = new ClientListener(server, this);
      listener.start();
      // Create the ObjectSender to the Server
      //ObjectOutputStream outToServer = new ObjectOutputStream(server.getOutputStream());
      synchronized (this) {
        try {
        //As long as the Client is connected to the Server
        while (connected) {
          //Wait until you get the GREEN LIGHT
          // so you can send it to the Server
          /*while (!readyToSend) { // readyToSend = true?? where??
            try {
              wait();
            } catch (InterruptedException e) {
              connected = false;
            }
          }*/
          wait();
          //outToServer.writeObject(request);
          //outToServer.flush();
          //readyToSend = false;
          //request = null;
        }
        //NOT CONNECTED ANYMORE
        outToServer.close();
        server.close();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
  }

  void applyGameInfoHandler(GameInfo gameinfo) {
    gameInfoHandler.applyGameInfo(gameinfo);

  }

  public synchronized void qeueRequest(Request request) {
    /*this.request = request;
    this.readyToSend = true;
    notifyAll();*/
    try {
      //out = new ObjectOutputStream(clientOut.getOutputStream());
      outToServer.writeObject(request);
      outToServer.flush();
    } catch (IOException e) {
      connected = false;
      notifyAll();
    }
  }

  public synchronized GameInfo getGameInfo() {
    GameInfo send = forwardToController;
    this.forwardToController = null;
    notifyAll();
    return send;
  }

  public synchronized void disconnect() {
    this.connected = false;
    readyToSend = true;
    request = null;
    notifyAll();
  }


  public String getUserName() {
    return username;
  }
}

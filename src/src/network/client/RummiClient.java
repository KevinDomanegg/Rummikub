package network.client;

import communication.Serializer;
import communication.request.Request;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class RummiClient {
  private static final int port = 48410;

  //Connection variables
  private Socket serverSocket;
  private PrintWriter outToServer;
  private ClientListener listener;
  private Serializer serializer;
  private GameInfoHandler gameInfoHandler;

  //CREATE A NEW CLIENT WITH USERNAME, AGE AND IP ADDRESS OF THE SERVER("localhost" or ip)
  public RummiClient(String serverIPAddress) throws IOException {
    serializer = new Serializer();
    serverSocket = new Socket(serverIPAddress, port);
    outToServer = new PrintWriter(serverSocket.getOutputStream());
  }

  public void setGameInfoHandler(GameInfoHandler gameInfoHandler) {
    this.gameInfoHandler = gameInfoHandler;
  }

  public void start() throws IOException {
    // Add a listener to this Client
    listener = new ClientListener(serverSocket.getInputStream(), this);
    listener.start();
  }

  public void disconnect() {
    System.out.println("From RummiClient: disconnecting client...");
    // notify listener that this client closes it instead of the server
    listener.disconnect();
    try {
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("From RummiClient: client closed");
  }

  public void sendRequest(Object request) {
    outToServer.println(serializer.serialize((Request) request));
    outToServer.flush();
  }

  void applyGameInfoHandler(Object gameInfo) {
    gameInfoHandler.applyGameInfo(gameInfo);
  }

  void notifyServerClose() {
    gameInfoHandler.notifyServerClose();
  }
}

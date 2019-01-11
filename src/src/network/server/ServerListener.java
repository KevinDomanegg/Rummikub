package network.server;

import communication.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerListener extends Thread {
  private Server server;
  private Socket clientIn;
  private int id;
  private boolean connected = false;
  private Request request;

  public ServerListener(Socket clientIn, Server request, int id) {
    this.clientIn = clientIn;
    this.server = request;
    this.id = id;
  }

  @Override
  public void run() {

    try {

      ObjectInputStream in = new ObjectInputStream(clientIn.getInputStream());
      this.connected = true;

      while (connected) {
        Object o = null;
        try {
          o = in.readObject();
          request = (Request) o;
        } catch (ClassNotFoundException | ClassCastException e) {
          connected = false;
        }

        if (o == null) {
          System.out.println("Client " + id + " not connected");
          this.connected = false;
          server.disconnectClient(id);
          break;
        }

        //request = (Game) o;
        System.out.println("Listener: Received " + request.toString());
        server.applyRequest(request);

//        for (int i = 0; i < Server.senders.length; i++) {
//          ServerSender s = Server.senders[i];
//          if (s != null && s.getClient() != this.clientIn) {
//            System.out.println("Listener: Sent message to Sender " + i);
//            s.send(request);
//          }
//        }
      }
    } catch (IOException e) {
      this.connected = false;
      server.disconnectClient(id);
    }
  }

  void disconnect() {
    this.connected = false;
  }
}

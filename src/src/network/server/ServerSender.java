package network.server;

import communication.GameInfo;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerSender extends Thread {
  private Socket clientOut;
  private RummiServer server;
  private int id;
  private boolean send = false;
  private boolean connected = true;
  private GameInfo info = null;


  public synchronized void send(GameInfo info) {
    this.info = info;
    send = true;
    notifyAll();
  }

  public ServerSender(Socket clientOut, RummiServer ser, int id) {
    this.clientOut = clientOut;
    this.server = ser;
    this.id = id;
  }


  Socket getClient() {
    return clientOut;
  }

  @Override
  public void run() {

    synchronized (this) {
      try {
        while (connected) {
          if (send) {
            ObjectOutputStream out = new ObjectOutputStream(clientOut.getOutputStream());
            out.writeObject(this.info);
            out.flush();
          }
          this.send = false;
          wait();
        }
      } catch (Exception e) {
        this.connected = false;
        server.disconnectClient(this.id);
      }
    }
  }

  void disconnect(){
    this.connected = false;
  }
}

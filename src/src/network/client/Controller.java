package network.client;

import communication.gameinfo.GameInfo;

import java.util.Scanner;

public class Controller {

  private GameInfo gameInfo;
  private RummiClient client;

   public Controller(RummiClient client) {
     this.client = client;
     this.client.start();
   }

   public void send() {
     //this.client.setSendToServer((Request) new Timer(InfoID.DRAW));
   }

   public static void main(String[] args) {
     Controller test = new Controller(new RummiClient("angelos", 21, "10.181.56.22"));
     GameInfo o;
     int alfa = 0;
     while (alfa < 9) {
        o = test.client.getGameInfo();
       if (o != null) {
         System.out.println(o);
         alfa++;
       }
       Scanner scanner = new Scanner(System.in);
       System.out.println("write command");
       String input = scanner.nextLine();
       if (input.equals("send")) {
         test.send();
         System.out.println("SENT SUCCESSFUL");
       }
     }
   }

}

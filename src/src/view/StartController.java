package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.client.RequestBuilder;
import network.client.RummiClient;
import network.server.RummiServer;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

/**
 * Class acting as the controller before a game has been started.
 * Connects the Start-view to the model.
 */
public class StartController {

    private RummiClient client;
    private RummiServer server;
    private Stage stage;

  private Main main;
  //private Media sound = new Media(new File("C:\\Users\\Angelos Kafounis\\Desktop\\rummikub---currygang\\src\\src\\view\\waitingMusic.mp3").toURI().toString());
  private Media sound;

  {
    try {
      sound = new Media((getClass().getResource("waitingMusic.mp3")).toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  private MediaPlayer mediaPlayer = new MediaPlayer(sound);


  @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField ipField;
    @FXML
    private StackPane vContainer;
    @FXML
    private AnchorPane errorPane;
    @FXML
    private Text errorMessage;

    @FXML
    public void initialize() {
        errorPane.setVisible(false);
    }

//    private String ip;
    //private String username;
    //private int username_id;
//    private String[] usernames = new String[4];

/*  StartController(Main networkController, ClientModel model, RummiClient client) {
    this.networkController = networkController;
    this.model = model;
    this.client = client;
  }*/

//    void setIpAddress(String ip) {
//        this.ip = ip;
//    }

//    void setUsername(String username, int username_id) {
//      if (waitController != null) {
//        waitController.setPlayerUsername(username, username_id);
//      } else {
//        usernames[username_id] = username;
//      }
//    }

    void returnToStart() {
      try {
        main.hostJoinStage(stage);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    @FXML
    void joinGame() throws IOException {
      if (nameField.getText().equals("")) {
        switchToErrorView("You must choose a username!");
      } else {
        switchToWait(new ClientModel(false));
        main.stopMusic();
      }
    }

    @FXML
    void hostGame() throws IOException {
      if (nameField.getText().equals("")) {
        try {
          switchToErrorView("You must have a username!");
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        server = new RummiServer();
        server.start();
        main.stopMusic();
        try {
          ipField.setText(Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
          e.printStackTrace();
        }
        switchToWait(new ClientModel(true));
      }
    }

    @FXML
    private void switchToErrorView(String message) throws IOException {
        errorMessage.setText(message);
        errorPane.setVisible(true);
        vContainer.setVisible(false);
    }

    void stopMusic() {
      mediaPlayer.stop();
    }

    private void switchToWait(ClientModel model) throws IOException {
        try {
        // Create local the Client and then pass it to: RequestBuilder and NetworkController
        client = new RummiClient(ipField.getText());
        if (!client.isServerOK()) {
          switchToErrorView("THERE IS NO SERVER YOU ASSHOLE!");
          return;
        }
        model.setServerIP(ipField.getText());
        // Create a RequestBuilder
        RequestBuilder reqBuilder = new RequestBuilder(client);

      NetworkController networkController = new NetworkController(client);
      networkController.setStartController(this);
      System.out.println("Client:" + nameField.getText() + " started");

      //A LITTLE MUSIC
      mediaPlayer.play();

        //Stage stage = (Stage) nameField.getScene().getWindow();
      stage = (Stage) nameField.getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("wait.fxml"));
        Parent root = loader.getRoot();
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        WaitController waitController = loader.getController();
        waitController.setNetworkController(networkController);
        networkController.setWaitController(waitController);
        waitController.setRequestBuilder(reqBuilder);
        waitController.setModel(model);

        // send request to set a player
        reqBuilder.sendSetPlayerRequest(nameField.getText(), Integer.parseUnsignedInt(ageField.getText()));

        Scene gameScene = new Scene(root, 1024, 768);
        stage.setScene(gameScene);
          stage.setOnCloseRequest(e -> {
            System.out.println("klicked  on x");
            killThreads();
            Platform.exit();
          });
        } catch(NumberFormatException ex) {
            switchToErrorView("Age has to be a number!");
        }
    }

    void killThreads() {
        if (client != null) {
            client.disconnect();
        }
        if (server != null) {
            server.suicide();
        }
    }

    @FXML
    void handleOkButton() {
        vContainer.setVisible(true);
        errorPane.setVisible(false);

    }

  public void setMain(Main main) {
      this.main = main;
  }
}

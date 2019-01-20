package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.client.RequestBuilder;
import network.client.RummiClient;
import network.server.RummiServer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * Class acting as the controller before a game has been started.
 * Connects the Start-view to the model.
 */
public class StartController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;

    @FXML
    private TextField ipField;

    private String ip;
    //private String username;
    //private int username_id;
    private WaitController waitController;
    private String[] usernames = new String[4];

/*  StartController(Main networkController, ClientModel model, RummiClient client) {
    this.networkController = networkController;
    this.model = model;
    this.client = client;
  }*/

    void setIpAddress(String ip) {
        this.ip = ip;
    }

    void setUsername(String username, int username_id) {
      if (waitController != null) {
        waitController.setPlayerUsername(username, username_id);
      } else {
        usernames[username_id] = username;
      }
    }

    @FXML
    void joinGame() throws IOException {
        switchToWait();
    }

    @FXML
    void hostGame() throws IOException {
        new RummiServer().start();
        switchToWait();
    }

    @FXML
    private void switchToErrorView() throws IOException {
        Stage stage = (Stage) ipField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Error.fxml"));
        Parent root = loader.load();
        Scene errorScene = new Scene(root, 600, 400);
        stage.setScene(errorScene);

    }


    private void switchToWait() {
        // Create local the Client and then pass it to: RequestBuilder and NetworkController
        RummiClient client = new RummiClient(ipField.getText());
        // Create a RequestBuilder
        RequestBuilder reqBuilder = new RequestBuilder(client);
        // send request to set a player
        reqBuilder.sendSetPlayerRequest(nameField.getText(), Integer.parseUnsignedInt(ageField.getText()));

        NetworkController networkController = new NetworkController(client);
        networkController.setStartController(this);
        System.out.println("Client:" + nameField.getText() + " started");

        Stage stage = (Stage) nameField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Wait.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        WaitController waitController = loader.getController();
        this.waitController = waitController;
        waitController.setIpAddress(ipField.getText());
        waitController.setNetworkController(networkController);
        networkController.setWaitController(waitController);
        waitController.setRequestBuilder(reqBuilder);

        waitController.setIpAddress(ip);
        for (int i = 0; i < usernames.length; i++) {
          if (usernames[i] != null) {
            waitController.setPlayerUsername(usernames[i], i);
          }
        }

        Scene gameScene = new Scene(root, 1024, 768);
        stage.setScene(gameScene);
    }
}

package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import network.client.GameInfoHandler;
import network.client.RummiClient;
import network.server.RummiServer;

import java.util.Observer;

/**
 * Class acting as the controller before a game has been started.
 * Connects the Start-view to the model.
 */
public class StartController {

  private ClientModel model;
  private RummiController rummiController;
  private RummiClient client;

  @FXML
  private TextField nameField;
  @FXML
  private TextField ageField;
  @FXML
  private Button joinButton;
  @FXML
  private Button hostButton;
  @FXML
  private TextField ipField;

  /**
   * Constructor connecting controller, model and network.
   *
   * @param rummiController the "master-controller" of the application
   * @param model storing all the relevant data
   * @param client connection-point to the network
   */
  StartController(RummiController rummiController, ClientModel model, RummiClient client) {
    this.rummiController = rummiController;
    this.model = model;
    this.client = client;
  }

  @FXML
  private void joinGame() {
    join(nameField.getText(), Integer.parseInt(ageField.getText()), ipField.getText());
  }

  @FXML
  private void hostGame() {
    new RummiServer().start();
    join(nameField.getText(), Integer.parseInt(ageField.getText()), "localhost");
  }

  private void join(String name, int age, String serverIP) {
    client = new RummiClient(name, age, serverIP);
    client.start();
    System.out.println("Client:" + name + " started");
    model.setName(name);
    model.setAge(age);
  }

}

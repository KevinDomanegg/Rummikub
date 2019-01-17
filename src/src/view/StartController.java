package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import network.client.GameInfoHandler;
import network.client.RummiClient;
import network.server.RummiServer;

import java.io.IOException;
import java.util.Observer;

public class StartController {

  private ClientModel model;
  private RummiController rummiController;
  private RummiClient client;
  private String username;
  private Integer age;
  @FXML private Button joinButton;

  @FXML
  private TextField nameField;
  @FXML
  private TextField ageField;

  @FXML
  private Button hostButton;
  @FXML
  private TextField ipField;

  private Stage stage;

/*  StartController(RummiController rummiController, ClientModel model, RummiClient client) {
    this.rummiController = rummiController;
    this.model = model;
    this.client = client;
  }*/

  @FXML
  void joinGame() throws IOException {
    stage = (Stage) joinButton.getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
    Parent root = loader.load();
    Scene gameScene = new Scene(root, 1024, 768);
    gameScene.getStylesheets().add("gameStyle.css");
    stage.setScene(gameScene);


    /*model.setName(nameField.getText());
    model.setAge(Integer.parseInt(ageField.getText()));*/

  }

  @FXML
  void hostGame() throws IOException {
    /*new RummiServer().start();
    join(nameField.getText(), Integer.parseInt(ageField.getText()), ipField.getText());
 */ }

  public void join(String name, int age, String serverIP) {
    client = new RummiClient(name, age, serverIP);
    client.start();
    System.out.println("Client:" + name + " started");
  }

}

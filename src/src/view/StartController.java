package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import network.client.GameInfoHandler;
import network.client.RummiClient;
import network.server.RummiServer;

import java.util.Observer;

public class StartController {

    private ClientModel model;

    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private Button joinButton;
    @FXML private Button hostButton;
    @FXML private TextField ipField;

    RummiClient client;
    private GameInfoHandler gameInfoHandler;
    private String username;
    private Integer age;

    @FXML
    void joinGame() {

        model.setName(nameField.getText());
        model.setAge(Integer.parseInt(ageField.getText()));

    }

    @FXML
    void hostGame() {
        new RummiServer().start();
        join(nameField.getText(), Integer.parseInt(ageField.getText()), ipField.getText());
    }

    public void join(String name, int age, String serverIP) {
        client = new RummiClient(name, age, serverIP);
        client.setGameInfoHandler(new GameInfoHandler(this));
        client.start();
        System.out.println("Client:" + name + " started");
    }

    void setGameInfoHandler(GameInfoHandler gameInfoHandler) {
        this.gameInfoHandler = gameInfoHandler;
    }

}

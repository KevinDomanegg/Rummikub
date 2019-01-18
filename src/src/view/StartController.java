package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import network.client.RummiClient;
import network.server.RummiServer;

import java.io.IOException;

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


/*  StartController(Main networkController, ClientModel model, RummiClient client) {
    this.networkController = networkController;
    this.model = model;
    this.client = client;
  }*/

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

    @FXML
    private void switchToWait() throws IOException {
        NetworkController networkController =
                new NetworkController(nameField.getText(),
                        Integer.parseUnsignedInt(ageField.getText()), ipField.getText());
        System.out.println("Client:" + nameField.getText() + " started");

        Stage stage = (Stage) nameField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Wait.fxml"));
        Parent root = loader.load();

        WaitController waitController = loader.getController();
        waitController.setNetworkController(networkController);
        networkController.setWaitController(waitController);

        Scene gameScene = new Scene(root, 1024, 768);
        stage.setScene(gameScene);
    }
}

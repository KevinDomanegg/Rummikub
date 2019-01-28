package view;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Class acting as the controller before a game has been started.
 * Connects the Start-view to the model.
 */
public class StartController {

  private Stage stage;

  //private Main main;
  //private Media sound;
  private MainController mainController;

//  {
//    try {
//      sound = new Media((getClass().getResource("waitingMusic.mp3")).toURI().toString());
//    } catch (URISyntaxException e) {
//      e.printStackTrace();
//    }
//  }

  //private MediaPlayer mediaPlayer = new MediaPlayer(sound);


  @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField ipField;
    @FXML
    private StackPane vContainer;
    @FXML
    private Text ipERROR;
    @FXML
    private Text ageERROR;
    @FXML
    private Text nameERROR;

    void setMainController(MainController mainController) {
      this.mainController = mainController;
    }

    void setError(String error) {
      switch(error) {
        case "ip":
          ipField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
          ipERROR.setVisible(true);
          return;
        case "age":
          ageField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
          ageERROR.setVisible(true);
          return;
        case "name":
          nameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
          nameERROR.setVisible(true);
      }
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

    void returnToStart(Stage primaryStage) {
//      primaryStagetry {
//        main.hostJoinStage(primaryStage); // new stage or just stage???
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
    }

    @FXML
    void joinGame() throws IOException {
      clearErrors();
      if (nameField.getText().equals("")) {
        setError("name");
        return;
      }
      try {
        int age = Integer.parseUnsignedInt(ageField.getText());
        if (age < 6 || age > 150) {
          throw new NumberFormatException();
        }
      } catch (NumberFormatException e) {
        setError("age");
        return;
      }
      mainController.initPlayer(ipField.getText(), nameField.getText(), Integer.parseUnsignedInt(ageField.getText()));
    }

    @FXML private void hostGame() throws IOException {
      clearErrors();
      if (nameField.getText().equals("")) {
        setError("name");
        return;
      }
      try {
        Integer.parseInt(ageField.getText());
      } catch (NumberFormatException e) {
        setError("age");
        return;
      }
      ipField.setText("localhost");
      mainController.startServer();
      //   switchToWait(new ClientModel(true));
      joinGame();
    }

    private void clearErrors() {
      ipField.setStyle(null);
      ipERROR.setVisible(false);
      nameField.setStyle(null);
      nameERROR.setVisible(false);
      ageField.setStyle(null);
      ageERROR.setVisible(false);
    }

  @FXML
  private void showHelpScene() {
      mainController.showHelpScene();
  }
}

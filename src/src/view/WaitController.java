package view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.music.Music;

public class WaitController implements Initializable {

//  private RequestBuilder requestBuilder;
//  private NetworkController networkController;
  private MainController mainController;

//  private ClientModel model;

  @FXML
  private Text waitingState;

  @FXML
  private Text ipAddress;

  @FXML
  private Button startGameButton;

  @FXML
  private Text player0;

  @FXML
  private Text player1;

  @FXML
  private Text player2;

  @FXML
  private Text player3;

  @FXML
  private Button notMuteButton;

  @FXML
  private Button muteButton;

  @FXML
  private HBox ipArea;

  private Stage stage;

  @FXML
  private void startGame() {
    mainController.sendStartRequest();
  }

  void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  Stage getStage() {
    return stage;
  }

  void setPlayerNames(List<String> names) {
    System.out.println("From WaitCtrl.: setting names.. " + names);
    switch (names.size()) { //TODO: Make readable
      case 4:
        player3.setText(names.get(3));
      case 3:
        player2.setText(names.get(2));
      case 2:
        player1.setText(names.get(1));
      case 1:
        player0.setText(names.get(0));
      default:
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }


  @FXML
  private void mute() {
    Music.muteSoundOfWait();
    muteButton.setVisible(false);
    notMuteButton.setVisible(true);
  }

  @FXML
  private void unMute() {
    Music.playMusicNow();
    notMuteButton.setVisible(false);
    muteButton.setVisible(true);
  }

  @FXML private void quitWaiting() {
    System.out.println("From QUIT in WaitCtrl.: disconnect client!");
    mainController.handleQuitPressed();
  }

  void setServerIP(String serverIP) {
    ipArea.setVisible(true);
    ipAddress.setText(serverIP);
  }

  @FXML
  private void showHelpScene() {
    mainController.showHelpScene();
  }
}

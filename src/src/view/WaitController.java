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
import view.music.Audio;

/**
 * Controller responsible for the waiting-scene that appears after joining a game.
 */
public class WaitController implements Initializable {

  private MainController mainController;

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

    /*
    * Switch distinguishing cases based on the number of players
    * that have already joined the game.
    */
    switch (names.size()) {
      case 4:
        player0.setText(names.get(0));
        player1.setText(names.get(1));
        player2.setText(names.get(2));
        player3.setText(names.get(3));
        break;
      case 3:
        player0.setText(names.get(0));
        player1.setText(names.get(1));
        player2.setText(names.get(2));
        player3.setText(ViewConstants.NO_PLAYER_SYMBOL);
        break;
      case 2:
        player0.setText(names.get(0));
        player1.setText(names.get(1));
        player2.setText(ViewConstants.NO_PLAYER_SYMBOL);
        player3.setText(ViewConstants.NO_PLAYER_SYMBOL);
        break;
      case 1:
        player0.setText(names.get(0));
        player1.setText(ViewConstants.NO_PLAYER_SYMBOL);
        player2.setText(ViewConstants.NO_PLAYER_SYMBOL);
        player3.setText(ViewConstants.NO_PLAYER_SYMBOL);
        break;
      default:
        break;
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }


  @FXML
  private void mute() {
    Audio.muteSoundOfWait();
    muteButton.setVisible(false);
    notMuteButton.setVisible(true);
  }

  @FXML
  private void unMute() {
    Audio.playMusicNow();
    notMuteButton.setVisible(false);
    muteButton.setVisible(true);
  }

  @FXML
  private void quitWaiting() {
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

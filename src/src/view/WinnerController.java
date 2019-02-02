package view;

import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * Controller responsible for the Winner-View.
 * Displays who has won the game.
 */
public class WinnerController {

  @FXML
  private ListView rankList;
  @FXML
  private Button quitButton;
  private MainController mainController;

  /**
   * Connects the WinnerController to a MainController.
   *
   * @param mainController to be connected to
   */
  void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  @FXML
  private void restartGame() {
    mainController.sendStartRequest();
  }

  @FXML
  private void quitGame() {
    ((Stage) quitButton.getScene().getWindow()).close();
  }

  /**
   * @ToDo
   * add javadoc; I don't understand the input, so I can't addd the javadoc
   */
  void setRank(Map<Integer, Integer> finalRank) {
    rankList.getItems().addAll(finalRank);
  }
}

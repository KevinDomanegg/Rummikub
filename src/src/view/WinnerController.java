package view;

import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class WinnerController {

  @FXML private ListView rankList;
  @FXML private Button quitButton;
  private MainController mainController;

  void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  @FXML private void restartGame() {
    mainController.sendStartRequest();
  }

  @FXML private void quitGame() {
    ((Stage) quitButton.getScene().getWindow()).close();
  }

  void setRank(Map<Integer, Integer> finalRank) {
    rankList.getItems().addAll(finalRank);
  }
}

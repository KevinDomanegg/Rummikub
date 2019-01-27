package view;

import java.util.List;
import java.util.Map.Entry;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class WinnerController {

  @FXML private ListView rankList;
  @FXML private Button quitButton;

  @FXML void restartGame() {
  }

  @FXML void quitGame() {
    ((Stage) quitButton.getScene().getWindow()).close();
  }

  void setRank(List<Entry<Integer, Integer>> finalRank) {
    rankList.getItems().addAll(finalRank);
  }
}

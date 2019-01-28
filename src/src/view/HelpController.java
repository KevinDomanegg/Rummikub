package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HelpController {

  @FXML
  private Button okButton;

  @FXML
  private void handleOkButton() {
      ((Stage) okButton.getScene().getWindow()).close();
  }
}

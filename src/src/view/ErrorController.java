package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;

public class ErrorController {

  @FXML
  private Text errorMessage;
  @FXML
  private Button okButton;

  @FXML
  void setErrorMessage(String message) {
    errorMessage.setText(message);
  }

  @FXML
  private void handleOkButton() {
    ((Stage) okButton.getScene().getWindow()).close();
  }
}

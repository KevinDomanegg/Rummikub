package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * Class acting as the controller before a game has been started.
 * Connects the Start-view to the model.
 */
public class StartController {
  private MainController mainController;

  @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField ipField;
    @FXML
    private StackPane vContainer;
    @FXML
    private Text ipError;
    @FXML
    private Text ageError;
    @FXML
    private Text nameError;

    void setMainController(MainController mainController) {
      this.mainController = mainController;
    }

    private void setError(String error) {
      switch(error) {
        case "ip":
          ipField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
          ipError.setVisible(true);
          return;
        case "age":
          ageField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
          ageError.setVisible(true);
          return;
        case "name":
          nameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
          nameError.setVisible(true);
      }
    }

    @FXML
    void joinGame() {
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

    @FXML private void hostGame() {
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
      joinGame();
    }

    private void clearErrors() {
      ipField.setStyle(null);
      ipError.setVisible(false);
      nameField.setStyle(null);
      nameError.setVisible(false);
      ageField.setStyle(null);
      ageError.setVisible(false);
    }

  @FXML
  private void showHelpScene() {
      mainController.showHelpScene();
  }

  void showNoServerError() {
    setError("ip");
  }
}

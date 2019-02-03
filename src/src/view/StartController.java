package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * Class acting as the controller before a game has been started.
 * Controlls the Start-View.
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

  /**
   * Connects the StartController to a MainController.
   * @param mainController to be connected to
   */
  void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  private void setError(String error) {
    switch (error) {
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

  /**
   * Creates a Player, connects to a server and joins a game.
   */
  @FXML
  void joinGame() {
    clearErrors();
    if (isValidInput()) {
      mainController.initPlayer(ipField.getText(), nameField.getText(), Integer.parseUnsignedInt(ageField.getText()));
    }
  }

  /**
   * Sets up a server, then joins that server.
   */
  @FXML
  private void hostGame() {
    clearErrors();
    if (isValidInput()) {
      ipField.setText("localhost");
      if (mainController.startServer()) {
        joinGame();
      }
    }
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

  /**
   * Shows an error-message indicating that no server is available.
   */
  void showNoServerError() {
    setError("ip");
  }

  private boolean isValidInput() {
    String userName = nameField.getText();
    boolean isValidInput = true;

    // Test name input
    if (userName.isEmpty() || userName.length() > 20) {
      setError("name");
      isValidInput = false;
    }

    // Test age input
    try {
      int age = Integer.parseInt(ageField.getText());
      if (age < 6 || age > 150) {
        setError("age");
        isValidInput = false;
      }
    } catch (NumberFormatException e) {
      setError("age");
      isValidInput = false;
    }
    return isValidInput;
  }
}

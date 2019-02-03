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

  /**
   * Enum for the different types of Errors that can occur.
   */
  enum ErrorType {IP, AGE, NAME}

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

  private void setError(ErrorType error) {
    switch (error) {
      case IP:
        ipField.setStyle(ViewConstants.ERROR_STYLE);
        ipError.setVisible(true);
        return;
      case AGE:
        ageField.setStyle(ViewConstants.ERROR_STYLE);
        ageError.setVisible(true);
        return;
      case NAME:
        nameField.setStyle(ViewConstants.ERROR_STYLE);
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
      ipField.setText(ViewConstants.LOCAL_IP);
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
    setError(ErrorType.IP);
  }

  private boolean isValidInput() {
    String userName = nameField.getText();
    boolean isValidInput = true;

    // Test name input
    if (userName.isEmpty() || userName.length() > 20) {
      setError(ErrorType.NAME);
      isValidInput = false;
    }

    // Test age input
    try {
      int age = Integer.parseInt(ageField.getText());
      if (age < 6 || age > 150) {
        setError(ErrorType.AGE);
        isValidInput = false;
      }
    } catch (NumberFormatException e) {
      setError(ErrorType.AGE);
      isValidInput = false;
    }
    return isValidInput;
  }
}

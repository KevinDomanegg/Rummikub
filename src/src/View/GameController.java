import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class GameController {
  @FXML
  Button drawButton;

  @FXML
  Text timer;

  public GameController() {}

  @FXML
  public void drawStone() {
    timer.setText("Hello!");
  }
}

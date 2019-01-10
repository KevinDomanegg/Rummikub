import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GameController {
  int handCount = 0;

  @FXML Button drawButton;
  @FXML Text timer;
  @FXML GridPane hand;
  @FXML StackPane opponentMid;

  String name = "Hannah";

  public GameController() {
  }

  @FXML
  public void drawStone() {
    // Server request: Get stone from bag
    Rectangle rectangle = new Rectangle(30,50);
    rectangle.setId("handStone" + handCount);
    handCount++;
    hand.add(rectangle, handCount,0);
  }

  public void nameChange() {
    //TODO: Doesnt work
    Node opMidName = opponentMid.getChildren().get(0);
    if (opMidName instanceof Text) {
      ((Text) opMidName).setText(name);
    }
  }


  //TODO: Remove this Stone class
  class Stone extends VBox {
    Stone() {

    }

  }

}

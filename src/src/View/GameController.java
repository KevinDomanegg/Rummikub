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
  @FXML GridPane handGrid;
  @FXML StackPane opponentMid;

  String name = "Hannah";

  public GameController() {
  }

  @FXML
  public void drawStone() {
    // Server request: Get stone from bag
    Rectangle rectangle = new Rectangle(30,50);
    rectangle.setId("handStone" + handCount);
    handGrid.add(rectangle, handCount,0);
    handCount++;
  }

  public void nameChange() {
    //TODO: Doesnt work
    Node opMidName = opponentMid.getChildren().get(0);
    if (opMidName instanceof Text) {
      ((Text) opMidName).setText(name);
    }
  }


  /** Constructs columns and rows for target GridPane outside of FXML
   *
   * @param target GridPane where the grid shall be constructed
   * @param columns amount of columns to be constructed
   * @param rows amount of rows to be constructed
   */
  /*
  void constructGrid(GridPane target, int columns, int rows) {
    for(int i = 0; i < columns; i++) {
      target.
    }
  }
  */

}

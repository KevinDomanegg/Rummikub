package View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;

public class GameController {
  int handCount = 0;

  @FXML Button drawButton;
  @FXML Text timer;
  @FXML GridPane table;
  @FXML GridPane handGrid;
  @FXML Pane opponentMid;
  @FXML Text testText;

  String name = "Player";

  public GameController() {
  }

  @FXML
  public void initialize() {
    constructGrid(table, 24, 8);
    constructGrid(handGrid, 20, 2);
    /*
    TESTING

    testText.setOnDragDetected(event -> {
      Dragboard dragBoard = testText.startDragAndDrop(TransferMode.ANY);
      ClipboardContent content = new ClipboardContent();
      content.putString(testText.getText());
      dragBoard.setContent(content);
    });
    */
  }

  @FXML
  public void drawStone() throws IOException {
    // Server request: Get stone from bag
    VBox cell = FXMLLoader.load(getClass().getResource("Stone.fxml"));

    /* TODO: Add eventHandler for drag and drop
    cell.setOnDragDetected(event -> {
      Dragboard dragBoard = cell.startDragAndDrop(TransferMode.ANY);
      ClipboardContent content = new ClipboardContent();
      content.put
      dragBoard.setContent(content);
    });
    */

    handGrid.add(cell, handCount, 0);
    handCount++;
  }

  public void nameChange() {
    Node opMidName = opponentMid.getChildren().get(0);
    if (opMidName instanceof Text) {
      ((Text) opMidName).setText(name);
    }
  }


  @FXML
  void constructGrid(GridPane grid, int columns, int rows) {
    for(int x = 0; x < columns; x++) {
      for(int y = 0; y < rows; y++) {
        StackPane cell = new StackPane();
        cell.setPrefWidth(1024.0/columns); //TODO: Configure for hand, too
        cell.setPrefHeight(768.0/rows);
        cell.getStyleClass().add("cell");
        grid.add(cell, x, y);
      }
    }
  }

}

package View;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class GameController {
  int handCount = 0;

  @FXML Button drawButton;
  @FXML Text timer;
  @FXML GridPane table;
  @FXML GridPane handGrid;
  @FXML Pane opponentMid;

  private final DataFormat stoneFormat = new DataFormat("View.Stone");

  String name = "Player";

  public GameController() {
  }

  /*
  model.getFirstPlayerName() for the names in opponentHand
   */

  @FXML
  private void initialize() {
    constructGrid(table, 24, 8);
    constructGrid(handGrid, 20, 2);
  }

  @FXML
  private void constructGrid(GridPane grid, int columns, int rows) {
    for(int x = 0; x < columns; x++) {
      for(int y = 0; y < rows; y++) {
        StackPane cell = constructCell();
        cell.getStyleClass().add("cell");
        grid.add(cell, x, y);
      }
    }
  }

  StackPane constructCell() {
    StackPane cell = new StackPane();
    Text content = new Text("");

    setupDragAndDrop(cell, content);

    cell.getChildren().add(content);
    return cell;
  }

  void addStoneToCell(Pane cell, Stone stone) {
    Text stoneText = new Text(Integer.toString(stone.getNumber()));
    stoneText.getStyleClass().add(stone.getColor().toString());
    cell.getChildren().add(stoneText);
  }

  void setupDragAndDrop(Pane cell, Text content) {
    //Enable drag, copy content to clipboard
    cell.setOnDragDetected(event -> {
      if (!content.getText().isEmpty()) {
        //Enable drag only when cell's content is not empty
        Dragboard dragboard = cell.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent clipboardContent = new ClipboardContent();
        String stoneNumber = content.getText();
        clipboardContent.putString(stoneNumber);
        dragboard.setContent(clipboardContent);
      }
      event.consume();
    });

    //Remove source node's content when drag is done
    cell.setOnDragDone(event -> {
      if (event.getTransferMode() == TransferMode.MOVE) {
        content.setText("");
      }
      event.consume();
    });

    //Enable drop at this cell
    cell.setOnDragOver(event -> {
      if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
        event.acceptTransferModes(TransferMode.MOVE);
      }
      event.consume();
    });

    /* TODO: Implement hover (nice to have)
    cell.setOnDragEntered(event -> {
      if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
        content.getStyleClass().add("dropHover");
        System.out.println("Hover");
      }
      event.consume();
    });

    cell.setOnDragExited(event -> {
      content.getStyleClass().remove("dropHover");
      System.out.println("No hover");
    });
    */

    cell.setOnDragDropped(event -> {
      Dragboard dragboard = event.getDragboard();
      boolean success = false;
      if (dragboard.hasString()) {
        content.setText(dragboard.getString());
        success = true;
      }
      event.setDropCompleted(success);
      event.consume();
    });
  }

  @FXML
  public void drawStone() {
    // Server request: Get stone from bag
    Node cell = handGrid.getChildren().get(handCount);
    int value = (int) (Math.random() * 10);
    Text text = new Text(Integer.toString(value));
    if (cell instanceof StackPane) {
      setupDragAndDrop((StackPane) cell, text);
      ((StackPane) cell).getChildren().add(text);
    }
    handCount++;
  }

  /*
  void setStone(Pane cell, String content) {
    Node textField = cell.getChildren().get(0); //TODO: Not safe!!!
    if (Text) textField).setText(content);
      //TODO: Set styleClass to set stone's color
    }
  }*/
}

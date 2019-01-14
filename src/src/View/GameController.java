package View;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;

public class GameController {
  int handCount = 0;

  @FXML Button drawButton;
  @FXML Text timer;
  @FXML GridPane table;
  @FXML GridPane handGrid;
  @FXML Pane opponentMid;

  String name = "Player";

  public GameController() {
  }

  @FXML
  public void initialize() {
    constructGrid(table, 24, 8);
    constructGrid(handGrid, 20, 2);
  }

  @FXML
  public void drawStone() throws IOException {
    // Server request: Get stone from bag
    VBox cell = FXMLLoader.load(getClass().getResource("Stone.fxml"));
    handGrid.add(cell, handCount, 0);
    handCount++;
  }

  @FXML
  void constructGrid(GridPane grid, int columns, int rows) {
    for(int x = 0; x < columns; x++) {
      for(int y = 0; y < rows; y++) {
        StackPane cell = new StackPane();
        //setupDragAndDrop(cell);
        cell.setPrefWidth(1024.0/columns); //TODO: Configure for hand, too
        cell.setPrefHeight(768.0/rows);
        cell.getStyleClass().add("cell");
        grid.add(cell, x, y);
      }
    }
  }

  /*
  void setupDragAndDrop(StackPane cell) {

    // Start drag here
    cell.setOnDragDetected(event -> {
      Dragboard dragBoard = cell.startDragAndDrop(TransferMode.ANY);
      ClipboardContent content = new ClipboardContent();
      // TODO: Put stone to clipboard
      if (!cell.getChildren().isEmpty()) {
        content.put(DataFormat.FILES, cell.getChildren());
        dragBoard.setContent(content);
      }
      event.consume();
    });

    // Accept drop here
    cell.setOnDragOver(event -> {
      event.acceptTransferModes(TransferMode.ANY);
      event.consume();
    });

    // Drop here
    cell.setOnDragDropped(event -> {
      Dragboard dragboard = event.getDragboard();
      if (dragboard.hasFiles()) {
        cell.getChildren().addAll(dragboard.getFiles());
      }
      //TODO: Put dragboard content as node children
      event.consume();
    });
  }
  */

}

package view;

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
    ClientModel model = new ClientModel();
    int handCount = 0;

    @FXML Button drawBut;
    @FXML Text timer;
    @FXML GridPane table;
    @FXML GridPane handGrid;
    @FXML Pane opponentMid;
    @FXML Text stupidTest;

    String name = "Player";

    public GameController() {
    }

    @FXML
    public void initialize() {
        constructGrid(table, 24, 8);
        constructGrid(handGrid, 20, 2);
        setupDrag(stupidTest);
    }

    @FXML
    public void drawStone() throws IOException {
        // Server request: Get stone from bag

    /*
    TODO: Set stone to desired value
    FXMLLoader loader = new FXMLLoader();
    VBox cell = loader.load(getClass().getResource("Stone.fxml"));
    StoneController stoneController = loader.getController();
    stoneController.setStone(3, "rot");
    */

        Text drawnStone = new Text("5");

        handGrid.add(drawnStone, handCount, 0);
        setupDrag(drawnStone);
        handCount++;
    }

    @FXML
    void constructGrid(GridPane grid, int columns, int rows) {
        for(int x = 0; x < columns; x++) {
            for(int y = 0; y < rows; y++) {
                StackPane cell = new StackPane();
                setupDrop(cell);

                cell.setPrefWidth(1024.0/columns); //TODO: Configure for hand, too
                cell.setPrefHeight(768.0/rows);
                cell.getStyleClass().add("cell");
                grid.add(cell, x, y);
            }
        }
    }


    void setupDrag(Text test) {
        // Start drag here
    /*
    test.setOnDragDetected(event -> {
      Dragboard dragBoard = test.startDragAndDrop(TransferMode.ANY);
      ClipboardContent content = new ClipboardContent();
      content.putString(test.getText());
      dragBoard.setContent(content);
      event.consume();
    });
    */

        test.setOnDragDetected(event -> {
            Dragboard db = test.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(test.getText());
            db.setContent(content);
            event.consume();
        });
    }

    void setupDrop(StackPane target) {
        // Accept drop here
        target.setOnDragOver(event -> {
            if (event.getGestureSource() != target && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

    /*
    // Drop here
    target.setOnDragDropped(event -> {
      Dragboard dragboard = event.getDragboard();
      if (dragboard.hasFiles()) {
        System.out.println(dragboard.toString());
        //target.getChildren().removeAll();
        Text content = new Text(dragboard.getString());
        target.getChildren().add(content);
      }
      event.consume();
    });
    */

        target.setOnDragOver(event -> {
            if (event.getGestureSource() != target &&
                    event.getDragboard().hasString()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
        });
    }

    // TODO: Remove test method
    void setupDrop(Text target) {
        // Accept drop here
        target.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
            event.consume();
        });

        // Drop here
        target.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                target.setText(dragboard.getString());
            }
            event.consume();
        });
    }

}

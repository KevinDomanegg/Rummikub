package View;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.List;

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

  private StackPane constructCell() {
    StackPane cell = new StackPane();

    Text number = new Text("");
    number.setUserData("number");
    Text color = new Text("");
    number.setUserData("color");
    color.setVisible(false);

    setupDragAndDrop(cell, number);

    cell.getChildren().add(number);
    return cell;
  }

  private void addStoneToCell(Pane cell, Stone stone) {
    Rectangle background = new Rectangle(20, 40);
    Text stoneText = new Text(Integer.toString(stone.getNumber()));
    stoneText.setUserData("number");
    Text stoneColor = new Text(stone.getColor().toString());
    stoneText.setUserData("color");
    stoneText.setVisible(false);

    cell.getChildren().add(background);
    cell.getChildren().add(stoneText);
    cell.getChildren().add(stoneColor);
  }

  private Node getNodeByUserData(Pane parent, Object userData) {
    List<Node> cellChildren = parent.getChildren();
    for (Node child : cellChildren) {
      if (child.getUserData() == userData) {
        return child;
      }
    }
    return null; //TODO
  }

  /*
  private Stone getStoneFromCell(Pane cell) {
    Node numberField = getNodeByUserData(cell, "number");
    int number = 0; //TODO
    if (numberField instanceof Text) {
      String numberInfo = ((Text) numberField).getText();
      number = Integer.parseInt(numberInfo);
    }

    Node colorField = getNodeByUserData(cell, "color");
    Stone stone = new Stone();
    if (colorField instanceof Text) {
      String colorInfo = ((Text) colorField).getText();
      switch (colorInfo) {
        case "BLACK":
          stone = new Stone(Stone.Color.BLACK, number);
          break;
        case "BLUE":
          stone = new Stone(Stone.Color.BLUE, number);
          break;
        case "YELLOW":
          stone = new Stone(Stone.Color.YELLOW, number);
          break;
        case "RED":
          stone = new Stone(Stone.Color.RED, number);
          break;
        case "JOKER":
          stone = new Stone();
          break;
        default:
      }
    }
    return stone;
  }
  */

  private void setupDragAndDrop(Pane cell) {
    //Enable drag, copy content to clipboard
    cell.setOnDragDetected(event -> {
      if (!cell.getChildren().isEmpty()) {
        //Enable drag only when cell's content is not empty
        Dragboard dragboard = cell.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent clipboardContent = new ClipboardContent();
        //TODO: Get stone from model
        //clipboardContent.put(stoneFormat, stone);
        dragboard.setContent(clipboardContent);
      }
      event.consume();
    });

    //Remove source node's content when drag is done
    cell.setOnDragDone(event -> {
      if (event.getTransferMode() == TransferMode.MOVE) {
        cell.getChildren().removeAll();
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
      if (dragboard.hasFiles()) {
        Stone stone = (Stone) dragboard.getContent(stoneFormat);
        addStoneToCell(cell, stone);
        success = true;
      }
      event.setDropCompleted(success);
      event.consume();
    });
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
    Pane cell = (Pane) handGrid.getChildren().get(handCount);
    int value = (int) (Math.random() * 10);
    Stone stone = new Stone(Stone.Color.BLUE, value);
    addStoneToCell(cell, stone);
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

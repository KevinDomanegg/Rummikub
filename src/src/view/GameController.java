package view;

import communication.gameinfo.StoneInfo;
import javafx.fxml.FXML;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import network.client.RequestBuilder;

import java.io.IOException;
import java.util.List;

public class GameController {
    private NetworkController networkController;
    private ClientModel model;
    private RequestBuilder requestBuilder;
    private DataFormat stoneFormat = new DataFormat("stoneFormat");

  @FXML Text timer;
  @FXML GridPane table;
  @FXML GridPane handGrid;
  @FXML Pane opponentMid;

  String name = "Player";

  void setNetworkController(NetworkController networkcontroller) {
    this.networkController = networkcontroller;
  }

    void setRequestBuilder(RequestBuilder requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    @FXML
    public void initialize() {
      model = new ClientModel();
      constructGrid(table, true, 24, 8);
      constructGrid(handGrid, false, 20, 2);
      putStoneInCell((Pane) handGrid.getChildren().get(0), new StoneInfo("black", 5)); //TODO: Remove
    }

  @FXML
  public void drawStone() throws IOException {
    networkController.sendDrawRequest();
    model.finishTurn();
    // Server request: Get stone from bag

    //TODO: Confirm drawn stone, update view
  }

  @FXML
  void constructGrid(GridPane grid, boolean isTable, int columns, int rows) {
    for(int x = 0; x < columns; x++) {
      for(int y = 0; y < rows; y++) {
        StackPane cell = new StackPane();
        setupDragAndDrop(cell, isTable);

        cell.setPrefWidth(1024.0/columns); //TODO: Configure for hand, too
        cell.setPrefHeight(768.0/rows);
        cell.getStyleClass().add("cell");
        grid.add(cell, x, y);
      }
    }
  }

  private void setupDragAndDrop(Pane cell, boolean isTable) {
    // Start drag and drop, copy stone to clipboard, delete stone in model
    cell.setOnDragDetected(event -> {
      Dragboard dragBoard = cell.startDragAndDrop(TransferMode.ANY);
      ClipboardContent content = new ClipboardContent();

      // Get stone from model
      StoneInfo[][] stoneGrid;
      StoneInfo cellContent = new StoneInfo("black", 5); //TODO: Remove
      int column = GridPane.getColumnIndex(cell);
      int row = GridPane.getRowIndex(cell);
      /* TODO: Enable
      if (isTable) {
        stoneGrid = model.getTable();
        cellContent = stoneGrid[column][row];
      } else {
        stoneGrid = model.getHand();
        cellContent = stoneGrid[column][row];
      }
      */

      // Put stone on clipboard
      content.put(stoneFormat, cellContent);
      dragBoard.setContent(content);
      //stoneGrid[column][row] = null; TODO: Enable
      event.consume();
    });

    // Enable cell to accept drop
    cell.setOnDragOver(event -> {
      if (event.getDragboard().hasContent(stoneFormat)) {
        event.acceptTransferModes(TransferMode.ANY);
      }
      event.consume();
    });

    //
    cell.setOnDragDropped(event -> {
      StoneInfo stoneInfo = (StoneInfo) event.getDragboard().getContent(stoneFormat); //TODO: Is parsing correct?
      putStoneInCell(cell, stoneInfo);
      event.consume();
    });
  }

  private void putStoneInCell(Pane cell, StoneInfo stone) {
    Rectangle stoneBackground = new Rectangle(20, 40);
    Text stoneValue = new Text(Integer.toString(stone.getNumber()));
    stoneValue.getStyleClass().add(stone.getColor()); //TODO: Configure style classes
    cell.getChildren().add(stoneBackground);
    cell.getChildren().add(stoneValue);
  }

  public void setTable(StoneInfo[][] table) {
    model.setTable(table);
  }

  public void setPlayerHand(StoneInfo[][] hand) {
    model.setHand(hand);
  }

  public void notifyInvalidMove() {

  }

  public void setBagSize(int bagSize) {
    model.setBagSize(bagSize);
  }

  public void notifyTurn() {
    model.notifyTurn();
  }

  public void setHandSizes(List<Integer> sizes) {
    model.setHandSizes(sizes);
  }

  public void setPlayerNames(List<String> names) {
    model.setPlayerNames(names);
  }

  public void notifyCurrentPlayer(int playerID) {
    model.setCurrentPlayer(playerID);
  }

  public void notifyGameStart() {
    model.notifyGameStart();
  }
}

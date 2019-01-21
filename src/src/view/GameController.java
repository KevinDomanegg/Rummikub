package view;

import communication.gameinfo.StoneInfo;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import network.client.RequestBuilder;

import java.util.List;

public class GameController {
  @FXML
  Text timer;
  @FXML
  private GridPane table;
  @FXML
  private GridPane handGrid;
  @FXML
  private Pane opponentMid;
  private NetworkController networkController;
  private ClientModel model = buildTestModel();
  private StoneInfo[][] tableData;
  private StoneInfo[][] handData;
  private RequestBuilder requestBuilder;
  private static DataFormat stoneFormat = new DataFormat("stoneFormat");

  void setNetworkController(NetworkController networkcontroller) {
    this.networkController = networkcontroller;
  }

  void setRequestBuilder(RequestBuilder requestBuilder) {
    this.requestBuilder = requestBuilder;
  }

  /* TODO: REMOVE TEST METHODS*/
  StoneInfo[][] buildTestTable(int columns, int rows) {
    StoneInfo[][] result = new StoneInfo[columns][rows];
    for (int x = 0; x < columns; x=x+2) {
      for (int y = 0; y < rows; y=y+2) {
        StoneInfo cell = new StoneInfo("red", 5);
        result[x][y] = cell;
      }
    }
    return result;
  }

  ClientModel buildTestModel() {
    ClientModel result = new ClientModel(false);
    result.setHand(buildTestTable(20,2));
    result.setTable(buildTestTable(40,8));
    return result;
  }

  public void returnToStart() {
    networkController.returnToStartView();
  }

  /**
   * This method is automatically called after the FXMLLoader loaded all FXML content.
   */
  @FXML
  public void initialize() {
    updateView();
  }

  /**
   * Updates FXML with data from model.
   */
  public void updateView() {
    constructGrid(table, true);
    constructGrid(handGrid, false);
  }

  /**
   * Method to request stone from server and place it in player's hand
   * event: User clicks draw button
   */
  @FXML
  public void drawStone() {
    networkController.sendDrawRequest();
    model.finishTurn();
    // Server request: Get stone from bag

    //TODO: Confirm drawn stone, update view
  }

  /**
   * Method to automatically construct columns, rows, and cells with StackPane in it.
   *
   * @param grid    The FXML GridPane where the cells shall be constructed in
   * @param isTable Indicator where a cell shall source its data from in case of drag and drop event
   */
  @FXML
  void constructGrid(GridPane grid, boolean isTable) {
    StoneInfo[][] currentGrid;
    if (isTable) {
      currentGrid = model.getTable();
    } else {
      currentGrid = model.getHand();
    }

    int columns = currentGrid.length;
    int rows = currentGrid[0].length;


    for (int x = 0; x < columns; x++) {
      for (int y = 0; y < rows; y++) {
        StackPane cell = new StackPane();

        if (currentGrid[x][y] != null) {
          StoneInfo stone = currentGrid[x][y];
          putStoneInCell(cell, stone);
        }

        cell.getStyleClass().add("cell");
        grid.add(cell, x, y);
        setupDragAndDrop(cell, isTable);
      }
    }
  }

  /**
   * Method to setup drag event, content to copy on clipboard, and drop event for a cell
   *
   * @param cell    Pane where the event shall be registered
   * @param isTable Indicator for whether the cells data source is the table grid - if not, it's the hand grid
   */
  private void setupDragAndDrop(Pane cell, boolean isTable) {
    // Get cell coordinates
    int thisColumn = GridPane.getColumnIndex(cell);
    int thisRow = GridPane.getRowIndex(cell);

    // Start drag and drop, copy stone to clipboard, delete stone in view
    cell.setOnDragDetected(event -> {
      Dragboard dragBoard = cell.startDragAndDrop(TransferMode.ANY);
      ClipboardContent content = new ClipboardContent();

      // Get stone from model
      StoneInfo[][] stoneGrid;
      if (isTable) {
        stoneGrid = model.getTable();
      } else {
        stoneGrid = model.getHand();
      }
      StoneInfo cellContent = stoneGrid[thisColumn][thisRow];

      if (cellContent != null) {
        cell.getChildren().clear();

        // Put stone on clipboard
        content.put(stoneFormat, cellContent);
        dragBoard.setContent(content);
        //stoneGrid[thisColumn][thisRow] = null;
        if (isTable) {
          model.setTable(stoneGrid);
        } else {
          model.setHand(stoneGrid);
        }
        //updateView();
      }
      event.consume();
    });

    // Enable cell to accept drop
    cell.setOnDragOver(event -> {
      if (event.getDragboard().hasContent(stoneFormat)) {
        event.acceptTransferModes(TransferMode.ANY);
      }
      event.consume();
    });

    // Put stone in target cell, notify server
    cell.setOnDragDropped(event -> {
      // Get source cell's coordinates
      Pane sourceCell = (Pane) event.getGestureSource();
      Parent sourceParent = sourceCell.getParent();
      int sourceColumn = GridPane.getColumnIndex(sourceCell);
      int sourceRow = GridPane.getRowIndex(sourceCell);

      Parent targetParent = cell.getParent();

      if (sourceParent.getId().equals("handGrid")) {
        if (targetParent.getId().equals("handGrid")) {
          requestBuilder.moveStoneOnHand(sourceColumn, sourceRow, thisColumn, thisRow);
        } else {
          requestBuilder.sendPutStoneRequest(sourceColumn, sourceRow, thisColumn, thisRow);
        }
      } else {
        requestBuilder.sendMoveStoneOnTable(sourceColumn, sourceRow, thisColumn, thisRow);
      }
      event.consume();



      /* TODO: Old code
      StoneInfo[][] stoneGrid; //TODO: This is replicated code
      if (isTable) {
        stoneGrid = model.getTable();
        stoneGrid[sourceColumn][sourceRow] = null;
        model.setTable(stoneGrid);
      } else {
        stoneGrid = model.getHand();
        stoneGrid[sourceColumn][sourceRow] = null;
        model.setHand(stoneGrid);
      }

      //Setting new cell
      StoneInfo stoneInfo = (StoneInfo) event.getDragboard().getContent(stoneFormat); //TODO: Is parsing correct?
      putStoneInCell(cell, stoneInfo);
      */
    });
  }

  /**
   * Method for displaying a stone in a cell
   *
   * @param cell  Cell in which the stone shall be displayed
   * @param stone Properties (color, value) of the stone which shall be displayed
   */
  private void putStoneInCell(Pane cell, StoneInfo stone) {
    Rectangle stoneBackground = new Rectangle(20, 40);
    stoneBackground.getStyleClass().add("stone");
    Text stoneValue = new Text(Integer.toString(stone.getNumber()));
    stoneValue.getStyleClass().add(stone.getColor());
    stoneValue.getStyleClass().add("stoneValue");
    cell.getChildren().add(stoneBackground);
    cell.getChildren().add(stoneValue);
  }

  public void setTable(StoneInfo[][] table) {
    model.setTable(table);
    constructGrid(this.table, true);
  }

  public void setPlayerHand(StoneInfo[][] hand) {
    model.setHand(hand);
    constructGrid(handGrid, false);
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

  /**
   * Method to update the data from the server.
   * Triggers view update
   *
   * @param model New model from server
   */
  public void setModel(ClientModel model) {
    this.model = model;
    updateView();
  }
}

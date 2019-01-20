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
      constructGrid(table, true, 24, 8);
      constructGrid(handGrid, false, 20, 2);
      putStoneInCell((Pane) handGrid.getChildren().get(0), new StoneInfo("black", 5)); //TODO: Remove
    }
  /**
   * This method is automatically called after the FXMLLoader loaded all FXML content.
   */
  @FXML
  public void initialize() {
    updateView();

    //TODO: Remove this line
    putStoneInCell((Pane) handGrid.getChildren().get(0), new StoneInfo("red", 5));
  }

  /**
   * Updates FXML with data from model.
   */
  public void updateView() {
    constructGrid(table, true, 24, 8);
    constructGrid(handGrid, false, 20, 2);
  }

  /** Method to request stone from server and place it in player's hand
   *  event: User clicks draw button
   */
  @FXML
  public void drawStone() {
    networkController.sendDrawRequest();
    model.finishTurn();
    // Server request: Get stone from bag

    //TODO: Confirm drawn stone, update view
  }

  /** Method to automatically construct columns, rows, and cells with StackPane in it.
   *
   * @param grid The FXML GridPane where the cells shall be constructed in
   * @param isTable Indicator where a cell shall source its data from in case of drag and drop event
   * @param columns Number of columns to be constructed
   * @param rows Number of rows to be constructed
   */
  @FXML
  void constructGrid(GridPane grid, boolean isTable, int columns, int rows) {
    for (int x = 0; x < columns; x++) {
      for (int y = 0; y < rows; y++) {
        StackPane cell = new StackPane();
        setupDragAndDrop(cell, isTable);

        cell.getStyleClass().add("cell");
        grid.add(cell, x, y);
      }
    }
  }

  /** Method to setup drag event, content to copy on clipboard, and drop event for a cell
   *
   * @param cell Pane where the event shall be registered
   * @param isTable Indicator for whether the cells data source is the table grid - if not, it's the hand grid
   */
  private void setupDragAndDrop(Pane cell, boolean isTable) {
    int columnIndex = GridPane.getColumnIndex(cell);
    int rowIndex = GridPane.getRowIndex(cell);

    // Start drag and drop, copy stone to clipboard, delete stone in model
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
      StoneInfo cellContent = stoneGrid[columnIndex][rowIndex];

      if (cellContent != null) {
        // Put stone on clipboard
        content.put(stoneFormat, cellContent);
        dragBoard.setContent(content);
        stoneGrid[columnIndex][rowIndex] = null;
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

    //
    cell.setOnDragDropped(event -> {
      // Delete source stone
      Pane sourceCell = (Pane) event.getGestureSource();
      int column = GridPane.getColumnIndex(sourceCell);
      int row = GridPane.getRowIndex(sourceCell);
      StoneInfo[][] stoneGrid; //TODO: This is replicated code
      if (isTable) {
        stoneGrid = model.getTable();
      } else {
        stoneGrid = model.getHand();
      }
      stoneGrid[column][row] = null;

      //Setting new cell
      StoneInfo stoneInfo = (StoneInfo) event.getDragboard().getContent(stoneFormat); //TODO: Is parsing correct?
      putStoneInCell(cell, stoneInfo);
      event.consume();
    });
  }

  /** Method for displaying a stone in a cell
   *
   * @param cell Cell in which the stone shall be displayed
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
    updateView();
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

  public void setModel(ClientModel model) {
    this.model = model;
  }
}

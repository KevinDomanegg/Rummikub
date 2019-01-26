package view;

import communication.gameinfo.StoneInfo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import view.music.Music;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {

  @FXML private Text player0Name;
  @FXML private Text player0Hand;
  @FXML private Text player1Name;
  @FXML private Text player2Name;
  @FXML private Text player3Name;
  @FXML private Text player1Hand;
  @FXML private Text player2Hand;
  @FXML private Text player3Hand;

  @FXML Text timer;
  @FXML private GridPane tableGrid;
  @FXML private GridPane handGrid;
  @FXML private VBox errorPane;
  @FXML private Text errorMessage;

//  private NetworkController networkController;
  private MainController mainController;
//  private ClientModel model;
//  private RequestBuilder requestBuilder;
  private static DataFormat stoneFormat = new DataFormat("stoneFormat");

  // MUSIC
  private Media sound_pickupStone;
  private Media sound_dropStone;
  private Media sound_drawStone;

  // TIMER
  private Timer timer_countDown;
  private TimerTask timer_task;

  // NO HOST AVAILABLE
  private boolean serverNotAvailable;




//  void setNetworkController(NetworkController networkcontroller) {
//    this.networkController = networkcontroller;
//  }

  void setMainController(MainController mainController) {
    this.mainController = mainController;
  }


//  void setRequestBuilder(RequestBuilder requestBuilder) {
//    this.requestBuilder = requestBuilder;
//  }

//  void setTimer() {
//    int delay = 1000;
//    int period = 1000;
//    timer_countDown = new Timer();
//    timer_countDown.scheduleAtFixedRate(
//        timer_task =
//            new TimerTask() {
//              int interval = 60;
//
//              public void run() {
//                if (interval == 0) {
//                  if (model.isMyTurn()) {
//                    requestBuilder.sendTimeOutRequest();
//                    model.finishTurn();
//                  }
//                  timer_countDown.cancel();
//                  timer_task.cancel();
//                  return;
//                }
//                timer.setText("" + interval);
//                interval--;
//              }
//            },
//        delay,
//        period);
//  }

  void stopTimer() {
    timer_task.cancel();
    timer_countDown.cancel();
  }

  /* TODO: REMOVE TEST METHODS*/
  StoneInfo[][] buildTestTable(int columns, int rows) {
    StoneInfo[][] result = new StoneInfo[columns][rows];
    for (int x = 0; x < columns; x = x + 2) {
      for (int y = 0; y < rows; y = y + 2) {
        StoneInfo cell = new StoneInfo("red", 5);
        result[x][y] = cell;
      }
    }
    return result;
  }

  public void returnToStart(boolean noServerAvailable) {
    if (noServerAvailable) {
      serverNotAvailable = true;
      showErrorView("THE HOST HAS LEFT THE GAME!");
    } else {
//      networkController.returnToStartView();
    }
  }

  public void quitGame() {
    mainController.quit();
  }

  /**
   * This method is automatically called after the FXMLLoader loaded all FXML content.
   */
  @FXML
  public void initialize() {
//    updateView();
    //putStoneInCell((Pane) handGrid.getChildren().get(0), new StoneInfo("red", 5));
  }

  /**
   * Updates FXML with data from model.
   */
  public void updateView() {
//    constructGrid(table, true);
//    constructGrid(handGrid, false);
  }

  /**
   * Method to request stone from server and place it in player's hand
   * event: User clicks draw button
   */
  @FXML
  public void drawStone() {
    Music.playSoundOf("draw stone");
    mainController.sendDrawRequest();
  }

  /**
   */
  @FXML
  void constructGrid(StoneInfo[][] stoneGrid, GridPane pane) {
    Platform.runLater(() -> pane.getChildren().clear());

    int width = stoneGrid.length;
    int height = stoneGrid[0].length;

    StoneInfo stoneInfo = null;
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        StackPane cell = new StackPane();

        if (stoneGrid[x][y] != null) {
          stoneInfo = stoneGrid[x][y];
          putStoneInCell(cell, stoneInfo);
        }

        int finalX = x;
        int finalY = y;
        StoneInfo finalStoneInfo = stoneInfo;
        Platform.runLater(() -> {
          cell.getStyleClass().add("cell");
          pane.add(cell, finalX, finalY);
          setupDragAndDrop(cell, finalStoneInfo);
        });
      }
    }
  }

  /**
   * Method to setup drag event, content to copy on clipboard, and drop event for a cell
   *  @param cell    Pane where the event shall be registered
   * @param stoneInfo Indicator for whether the cells data source is the table grid - if not, it's the hand grid
   */
  private void setupDragAndDrop(Pane cell, StoneInfo stoneInfo) {
    // Get cell coordinates
    int thisColumn = GridPane.getColumnIndex(cell);
    int thisRow = GridPane.getRowIndex(cell);

    // Start drag and drop, copy stone to clipboard, delete stone in view
    cell.setOnDragDetected(event -> {
//      if (!model.isMyTurn()) {
//        return;
//      }
      Music.playSoundOf("pick up stone");
      Dragboard dragBoard = cell.startDragAndDrop(TransferMode.ANY);
      ClipboardContent content = new ClipboardContent();

      if (stoneInfo != null) {
        // Put stone on clipboard
        content.put(stoneFormat, stoneInfo);
        dragBoard.setContent(content);
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
      Music.playSoundOf("drop stone");

      Pane sourceCell = (Pane) event.getGestureSource();
      sourceCell.getChildren().clear();
      int sourceColumn = GridPane.getColumnIndex(sourceCell);
      int sourceRow = GridPane.getRowIndex(sourceCell);

        Parent sourceParent = sourceCell.getParent();
        Parent targetParent = cell.getParent();

        if (sourceParent.getId().equals("handGrid")) {
          if (targetParent.getId().equals("handGrid")) {
            mainController.sendMoveStoneOnHand(sourceColumn, sourceRow, thisColumn, thisRow);
          } else {
            mainController.sendPutStoneRequest(sourceColumn, sourceRow, thisColumn, thisRow);
          }
        } else {
          mainController.sendMoveStoneOnTable(sourceColumn, sourceRow, thisColumn, thisRow);
        }
      event.consume();
    });
  }

  /**
   * Method for displaying a stone in a cell
   *
   * @param cell  Cell in which the stone shall be displayed
   * @param stone Properties (color, value) of the stone which shall be displayed
   */
  private void putStoneInCell(Pane cell, StoneInfo stone) {
    cell.getChildren().clear();
    Rectangle stoneBackground = new Rectangle(20, 40);
    stoneBackground.getStyleClass().add("stone");
    Text stoneValue = new Text(Integer.toString(stone.getNumber()));
    stoneValue.getStyleClass().add(stone.getColor());
    stoneValue.getStyleClass().add("stoneValue");
    cell.getChildren().add(stoneBackground);
    cell.getChildren().add(stoneValue);
    //STOP MUSIC
  }

  void setTable(StoneInfo[][] table) {
    constructGrid(table, tableGrid);
  }

  void setPlayerHand(StoneInfo[][] hand) {
    constructGrid(hand, handGrid);
  }

  void notifyInvalidMove() {

  }

//  void setBagSize(int bagSize) {
//    model.setBagSize(bagSize);
//  }

//  void notifyTurn() {
//    model.notifyTurn();
//  }

  void setHandSizes(List<Integer> sizes) {
    switch (sizes.size()) {
      case 4:
        player3Hand.setText(sizes.get(3).toString());
      case 3:
        player2Hand.setText(sizes.get(2).toString());
      case 2:
        player1Hand.setText(sizes.get(1).toString());
      case 1:
        player0Hand.setText(sizes.get(0).toString());
      default:
    }
  }

  void setPlayerNames(List<String> names) {
    switch (names.size()) {
      case 4:
        player3Name.setText(names.get(3));
      case 3:
        player2Name.setText(names.get(2));
      case 2:
        player1Name.setText(names.get(1));
      case 1:
        player0Name.setText(names.get(0));
      default:
    }
  }

  void notifyCurrentPlayer(int playerID) {
//    model.setCurrentPlayer(playerID);
//    // set up the timer
//    timer_countDown.cancel();
//    timer_task.cancel();
//    setTimer();
//    // show current player on view
  }

  @FXML
  private void sendResetRequest() {
    mainController.sendResetRequest();
  }

  @FXML
  private void sendConfirmMoveRequest() {
    mainController.sendConfirmMoveRequest();
  }

  //TODO
  private void showErrorView(String message) {
    errorMessage.setText(message);
    errorPane.setVisible(true);
    tableGrid.setVisible(false);
  }

  @FXML private void handleOkButton() {
    if (serverNotAvailable) {
      returnToStart(false);
    } else {
      errorPane.setVisible(false);
      tableGrid.setVisible(true);
    }
  }

  @FXML
  private void sendSortHandByGroupRequest() {
    mainController.sendSortHandByGroupRequest();
  }
  @FXML
  private void sendSortHandByRunRequest() {
    mainController.sendSortHandByRunRequest();
  }

//  @FXML private void sendSortHandByGroupRequest() {
//    requestBuilder.sendSortHandByGroupRequest();
//  }

//  @FXML private void sendSortHandByRunRequest() {
//    requestBuilder.sendSortHandByRunRequest();
//  }
}

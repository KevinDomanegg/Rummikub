package view;

import communication.gameinfo.StoneInfo;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import view.music.Music;

public class GameController {

  @FXML private HBox opponentRight;
  @FXML private HBox opponentMid;
  @FXML private HBox opponentLeft;

  @FXML private Text ownName;
  @FXML private Text ownHand;
  @FXML private Text leftPlayerName;
  @FXML private Text midPlayerName;
  @FXML private Text rightPlayerName;
  @FXML private Text leftPlayerHand;
  @FXML private Text midPlayerHand;
  @FXML private Text rightPlayerHand;

  @FXML private Text timer;
  @FXML private GridPane tableGrid;
  @FXML private GridPane handGrid;
  @FXML private VBox errorPane;
  @FXML private Text errorMessage;

//  private NetworkController networkController;
  private MainController mainController;
//  private ClientModel model;
//  private RequestBuilder requestBuilder;
  private static DataFormat stoneFormat = new DataFormat("stoneFormat");

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

  public void returnToStart(boolean noServerAvailable) {
    if (noServerAvailable) {
      serverNotAvailable = true;
      showErrorView("THE HOST HAS LEFT THE GAME!");
    } else {
      quitGame();
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
   * Method to automatically construct columns, rows, and cells with StackPane in it.
   *
//   * @param grid    The FXML GridPane where the cells shall be constructed in
//   * @param isTable Indicator where a cell shall source its data from in case of drag and drop event
//   */
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
   *
//   * @param cell    Pane where the event shall be registered
//   * @param isTable Indicator for whether the cells data source is the table grid - if not, it's the hand grid
//   */
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
      Image cellSnapshot = cell.snapshot(new SnapshotParameters(), null);
      dragBoard.setDragView(cellSnapshot, cell.getWidth()*0.5, cell.getHeight()*0.9); //TODO: Remove magic numbers? Only for cursor pos tho
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

    cell.setOnDragEntered(event -> {
      cell.setStyle("-fx-background-color: #FFFFFF44");
    });

    cell.setOnDragExited(event -> {
      cell.setStyle("-fx-background-color: none");
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
    stoneValue.getStyleClass().addAll("stoneValue", stone.getColor());
    cell.getChildren().addAll(stoneBackground, stoneValue);
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
    ownHand.setText(String.valueOf(sizes.get(0)));
    switch (sizes.size()) {
      case 4:
        leftPlayerHand.setText(String.valueOf(sizes.get(1)));
        midPlayerHand.setText(String.valueOf(sizes.get(2)));
        rightPlayerHand.setText(String.valueOf(sizes.get(3)));
        return;
      case 3:
        leftPlayerHand.setText(String.valueOf(sizes.get(1)));
        rightPlayerHand.setText(String.valueOf(sizes.get(2)));
        return;
      case 2:
        midPlayerHand.setText(String.valueOf(sizes.get(1)));
      default:
    }
  }

  void setPlayerNames(List<String> names) {
    ownName.setText(names.get(0));
    switch (names.size()) {
      case 4:
        leftPlayerName.setText(names.get(1));
        midPlayerName.setText(names.get(2));
        rightPlayerName.setText(names.get(3));
        return;
      case 3:
        opponentMid.setVisible(false);
        leftPlayerName.setText(names.get(1));
        rightPlayerName.setText(names.get(2));
        return;
      case 2:
      opponentMid.setVisible(true);
      opponentLeft.setVisible(false);
      opponentRight.setVisible(false);
      midPlayerName.setText(names.get(1));
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

  void showRanke(List<Entry<Integer, Integer>> finalRank) {

  }

  public void showRank() {
    List<Entry<Integer, Integer>> rank = new ArrayList<>();
    rank.add(new SimpleEntry<>(1, 0));
    rank.add(new SimpleEntry<>(0, -30));
    mainController.showRank(rank);
  }
}

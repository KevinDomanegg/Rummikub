package view;

import communication.gameinfo.StoneInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
  @FXML private Text bagSize;

  @FXML private Text timer;
  @FXML private GridPane tableGrid;
  @FXML private GridPane handGrid;
  @FXML private VBox errorPane;
  @FXML private Text errorMessage;
  @FXML private VBox ownBoard;

  @FXML private HBox moveButtons;
  @FXML private HBox backButtons;


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

  // Cntrl + Drag and Drop
  private boolean ctrl;


//  void setNetworkController(NetworkController networkcontroller) {
//    this.networkController = networkcontroller;
//  }

  void setMainController(MainController mainController) {
    this.mainController = mainController;
  }


//  void setRequestBuilder(RequestBuilder requestBuilder) {
//    this.requestBuilder = requestBuilder;
//  }

  private void setTimer() {
    int delay = 1000;
    int period = 1000;
    timer_countDown = new Timer();
    timer_countDown.scheduleAtFixedRate(
        timer_task = new TimerTask() {
              int interval = 10;
              public void run() {
                if (interval == 0) {
                  if (ownBoard.getStyle().equals("-fx-border-color: white; -fx-border-width: 4px ;")) {
                    stopTimer();
                    sendTimeOutRequest();
                    return;
                  }
                  stopTimer();
                  return;
                }
                timer.setText("" + interval);
                interval--;
              }
            },
        delay,
        period);
  }

  void stopTimer() {
    timer_task.cancel();
    timer_countDown.cancel();
  }

  public void returnToStart(boolean noServerAvailable) {
    if (noServerAvailable) {
      serverNotAvailable = true;
//      showError("THE HOST HAS LEFT THE GAME!");
    } else {
      quitGame();
    }
  }

  public void quitGame() {
    mainController.quit();
  }

  void yourTurn() {
    ownBoard.setStyle("-fx-border-color: white; -fx-border-width: 4px ;");
    //moveButtons.setVisible(true);
    //backButtons.setVisible(true);
  }

  private void endOfYourTurn() {
    ownBoard.setStyle("-fx-border-color: black; -fx-border-width: 4px ;");
    //moveButtons.setVisible(false);
    //backButtons.setVisible(false);
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
    endOfYourTurn();
  }

  /**
   * Method to automatically construct columns, rows, and cells with StackPane in it.
   *
   * @param stoneGrid    The FXML GridPane where the cells shall be constructed in
   * @param pane Indicator where a cell shall source its data from in case of drag and drop event
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
   *
   * @param cell    Pane where the event shall be registered
   * @param stoneInfo Indicator for whether the cells data source is the table grid - if not, it's the hand grid
   */
  private void setupDragAndDrop(Pane cell, StoneInfo stoneInfo) {
    // Get cell coordinates
    int thisColumn = GridPane.getColumnIndex(cell);
    int thisRow = GridPane.getRowIndex(cell);

    // Start drag and drop, copy stone to clipboard, delete stone in view
    cell.setOnDragDetected(event -> {
      System.out.println(stoneInfo);
      if (stoneInfo == null) {
        return;
      }

      if (event.isControlDown()) {
        System.out.println("----------------------------control pushed");
        ctrl = true;
      }


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
        System.out.println("control pressed is: ------- " + ctrl);
        if (ctrl) {
          mainController.sendMoveSetOnTableRequest(sourceColumn, sourceRow, thisColumn, thisRow);
          ctrl = false;
        } else {
          mainController.sendMoveStoneOnTable(sourceColumn, sourceRow, thisColumn, thisRow);
        }
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

  void setBagSize(int bagSize) {
    this.bagSize.setText(Integer.toString(bagSize));
  }

//  void notifyTurn() {
//    model.notifyTurn();
//  }

  void setHandSizes(List<Integer> sizes) {
    String handComplement = " Stones";
    ownHand.setText(String.valueOf(sizes.get(0)) + handComplement);
    switch (sizes.size()) {
      case 4:
        leftPlayerHand.setText(String.valueOf(sizes.get(1)) + handComplement);
        midPlayerHand.setText(String.valueOf(sizes.get(2)) + handComplement);
        rightPlayerHand.setText(String.valueOf(sizes.get(3)) + handComplement);
        return;
      case 3:
        leftPlayerHand.setText(String.valueOf(sizes.get(1)) + handComplement);
        rightPlayerHand.setText(String.valueOf(sizes.get(2)) + handComplement);
        return;
      case 2:
        midPlayerHand.setText(String.valueOf(sizes.get(1)) + handComplement);
      default:
    }
  }

  void setPlayerNames(List<String> names) {
    String nameComplement = ": ";
    ownName.setText(names.get(0) + nameComplement);
    switch (names.size()) {
      case 4:
        leftPlayerName.setText(names.get(1) + nameComplement);
        midPlayerName.setText(names.get(2) + nameComplement);
        rightPlayerName.setText(names.get(3) + nameComplement);
        return;
      case 3:
        opponentMid.setVisible(false);
        leftPlayerName.setText(names.get(1) + nameComplement);
        rightPlayerName.setText(names.get(2) + nameComplement);
        return;
      case 2:
      opponentMid.setVisible(true);
      opponentLeft.setVisible(false);
      opponentRight.setVisible(false);
      midPlayerName.setText(names.get(1) + nameComplement);
      default:
    }
  }

  @FXML
  void notifyCurrentPlayer(int relativeOpponentPosition) {
    if (timer_countDown != null) {
      timer_countDown.cancel();
      timer_task.cancel();
    }
    setTimer();
    HBox[] opponents = new HBox[] {
            opponentLeft, opponentMid, opponentRight
    };

    int opponentID = toOpponentID(relativeOpponentPosition, opponents);

    //sets the color of all the opponents
    for (int i = 1; i < opponents.length + 1; i++) {
      if (i != opponentID) {
        //styling non-playing opponents
        opponents[i - 1].setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
      } else {
        //styling currently playing opponent
        opponents[i - 1].setBackground(new Background((new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY))));
      }
    }
  }

  /**
   * Calculates the id of an opponent based on his relative postion to the player.
   *
   * @param relativeOpponentPosition number of steps (clockwise) until you reach the opponent
   * @return position of the opponent
   *          0 -> self
   *          1 -> left
   *          2 -> middle/top
   *          3 -> right
   */
  private int toOpponentID(int relativeOpponentPosition, HBox[] opponents) {

    //initialize with 1 (the client himself)
    int numOfPlayers = 1;
    for (HBox opponent : opponents) {
      if (opponent.isVisible()) {
        numOfPlayers++;
      }
    }
    System.out.println("num of player is " + numOfPlayers);
    System.out.println("relative position is  " + relativeOpponentPosition);

    int opponentID = 0;
    switch (numOfPlayers) {
      case 0:
        opponentID = 0;
        break;

      case 1:
        opponentID = 0;
        break;

      case 2:
        opponentID = relativeOpponentPosition % 2;
        if (opponentID == 1) {
          opponentID = 2;
        }
        break;

      case 3:

        if (relativeOpponentPosition == 3) {
          opponentID = 0;
        }

        else if (relativeOpponentPosition == 2) {
          opponentID = 3;
        } else {
          opponentID = relativeOpponentPosition;
        }
        break;

      case 4:
        opponentID = relativeOpponentPosition;
        break;

      default:
        opponentID = relativeOpponentPosition;

    }
    System.out.println("OpponentPosition is " + opponentID);
    return opponentID;
  }

  private void sendTimeOutRequest() {
    mainController.sendTimeOutRequest();
    System.out.println("--------------------------TIME_OUT");
    endOfYourTurn();
  }

  @FXML
  private void sendResetRequest() {
    mainController.sendResetRequest();
  }

  @FXML
  private void sendConfirmMoveRequest() {
    mainController.sendConfirmMoveRequest();
    endOfYourTurn();
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
    Map<Integer, Integer> rank = new HashMap<>();
    rank.put(1, 0);
    rank.put(0, -30);
    mainController.showRank(rank);
  }

  @FXML
  private void showHelpScene() {
    mainController.showHelpScene();
  }

  @FXML
  private void sendUndoRequest() {
    mainController.sendUndoRequest();
  }
}

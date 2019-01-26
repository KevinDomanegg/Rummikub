package view;

import communication.gameinfo.StoneInfo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import network.client.RequestBuilder;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {

  @FXML private Label player0Name;
  @FXML private Label player0Hand;
  @FXML private Label player1Name;
  @FXML private Label player2Name;
  @FXML private Label player3Name;
  @FXML private Label player1Hand;
  @FXML private Label player2Hand;
  @FXML private Label player3Hand;

  @FXML
  Text timer;
  @FXML
  private GridPane table;
  @FXML
  private GridPane handGrid;
  @FXML
  private VBox errorPane;
  @FXML
  private Text errorMessage;

  private NetworkController networkController;
  private ClientModel model;
  private RequestBuilder requestBuilder;
  private static DataFormat stoneFormat = new DataFormat("stoneFormat");

  //MUSIC
  private Media sound_pickupStone;
  private Media sound_dropStone;
  private Media sound_drawStone;

  //TIMER
  private Timer timer_countDown;
  private TimerTask timer_task;

  //NO HOST AVAILABLE
  private boolean serverNotAvailable;

  {
    try {
      sound_pickupStone = new Media((getClass().getResource("pickupStone.mp3")).toURI().toString());
      sound_dropStone = new Media((getClass().getResource("dropStone.mp3")).toURI().toString());
      sound_drawStone = new Media((getClass().getResource("drawStone.mp3")).toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }
  private MediaPlayer mediaPlayer_pickupStone = new MediaPlayer(sound_pickupStone);
  private MediaPlayer mediaPlayer_dropStone = new MediaPlayer(sound_dropStone);
  private MediaPlayer mediaPlayer_drawStone = new MediaPlayer(sound_drawStone);


  void setNetworkController(NetworkController networkcontroller) {
    this.networkController = networkcontroller;
  }

  void setRequestBuilder(RequestBuilder requestBuilder) {
    this.requestBuilder = requestBuilder;
  }

  void setTimer() {
    int delay = 1000;
    int period = 1000;
    timer_countDown = new Timer();
    timer_countDown.scheduleAtFixedRate(timer_task = new TimerTask() {
      int interval = 60;
      public void run() {
        if (interval == 0) {
          if (model.isMyTurn()) {
            requestBuilder.sendTimeOutRequest();
            model.finishTurn();
          }
          timer_countDown.cancel();
          timer_task.cancel();
          return;
        }
        timer.setText("" + interval);
        interval--;
      }
    }, delay, period);
  }

  void stopTimer() {
    timer_task.cancel();
    timer_countDown.cancel();
  }

  public void returnToStart(boolean noServerAvailable) {
    if (noServerAvailable) {
      serverNotAvailable = true;
      showErrorView("THE HOST HAS LEFT THE GAME!");
    } else {
      networkController.returnToStartView();
    }
  }


  public void quitButton() {
    Platform.runLater(() -> {
      returnToStart(false);
    });
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
    constructGrid(table, true);
    constructGrid(handGrid, false);
  }

  /**
   * Method to request stone from server and place it in player's hand
   * event: User clicks draw button
   */
  @FXML
  public void drawStone() {
    if (model.isMyTurn()) {
      mediaPlayer_drawStone.stop();
      mediaPlayer_drawStone.play();
      // Server request: Get stone from bag
      networkController.sendDrawRequest();
      model.finishTurn();
    } else {
      showErrorView("Error! It is not your turn");
    }
  }

  /**
   * Method to automatically construct columns, rows, and cells with StackPane in it.
   *
   * @param grid    The FXML GridPane where the cells shall be constructed in
   * @param isTable Indicator where a cell shall source its data from in case of drag and drop event
   */
  @FXML
  void constructGrid(GridPane grid, boolean isTable) {
    Platform.runLater(() -> {
      grid.getChildren().clear();
    });

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

        int finalX = x;
        int finalY = y;
        Platform.runLater(() -> {
          cell.getStyleClass().add("cell");
          grid.add(cell, finalX, finalY);
          setupDragAndDrop(cell, isTable);
        });
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
      if (!model.isMyTurn()) {
        return;
      }
      mediaPlayer_pickupStone.stop();
      mediaPlayer_pickupStone.play();
      Dragboard dragBoard = cell.startDragAndDrop(TransferMode.ANY);
      Image cellSnapshot = cell.snapshot(new SnapshotParameters(), null);
      dragBoard.setDragView(cellSnapshot, cell.getWidth()*0.5, cell.getHeight()*0.9); //TODO: Remove magic numbers? Only for cursor pos tho
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
        // Put stone on clipboard
        content.put(stoneFormat, cellContent);
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
      mediaPlayer_dropStone.stop();
      mediaPlayer_dropStone.play();

      Dragboard dragboard = event.getDragboard();
      StoneInfo sourceStone = (StoneInfo) dragboard.getContent(stoneFormat);

      // Get source cell's coordinates

      Pane sourceCell = (Pane) event.getGestureSource();
      sourceCell.getChildren().clear();
      int sourceColumn = GridPane.getColumnIndex(sourceCell);
      int sourceRow = GridPane.getRowIndex(sourceCell);


        Parent sourceParent = sourceCell.getParent();
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
    cell.getChildren().add(stoneBackground);
    cell.getChildren().add(stoneValue);
    //STOP MUSIC
  }

  void setTable(StoneInfo[][] table) {
    model.setTable(table);
    Platform.runLater(() -> {
      constructGrid(this.table, true);
    });
  }

  void setPlayerHand(StoneInfo[][] hand) {
    model.setHand(hand);
    Platform.runLater(() -> {
      constructGrid(handGrid, false);
    });
  }

  void notifyInvalidMove() {

  }

  void setBagSize(int bagSize) {
    model.setBagSize(bagSize);
  }

  void notifyTurn() {
    model.notifyTurn();
  }

  void setHandSizes(List<Integer> sizes) {
    model.setHandSizes(sizes);
    Platform.runLater(() -> {
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
    });
  }

  void setPlayerNames(List<String> names) {
    player3Name.setText("H");
    Platform.runLater(() -> {
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
    });
  }

  void notifyCurrentPlayer(int playerID) {
    model.setCurrentPlayer(playerID);
    // set up the timer
    timer_countDown.cancel();
    timer_task.cancel();
    setTimer();
    // show current player on view
  }

  /**
   * Method to update the data from the server.
   * Triggers view update
   *
   * @param model New model from server
   */
  void setModel(ClientModel model) {
    this.model = model;
    setPlayerNames(model.getPlayersNames());
    setHandSizes(model.getHandSizes());
    updateView();
  }

  @FXML
  private void sendResetRequest() {
    if (model.isMyTurn()) {
      requestBuilder.sendResetRequest();
      //model.finishTurn();
    } else {
      // error
    }
  }

  @FXML
  private void sendConfirmMoveRequest() {
    if (model.isMyTurn()) {
      requestBuilder.sendConfirmMoveRequest();
      model.finishTurn();
    } else {
      showErrorView("Error! It is not your turn");
    }
  }

  private void showErrorView(String message) {
    errorMessage.setText(message);
    errorPane.setVisible(true);
    table.setVisible(false);
  }

  @FXML private void handleOkButton() {
    if (serverNotAvailable) {
      returnToStart(false);
    } else {
      errorPane.setVisible(false);
      table.setVisible(true);
    }
  }

  @FXML private void sendSortHandByGroupRequest() {
    requestBuilder.sendSortHandByGroupRequest();
  }

  @FXML private void sendSortHandByRunRequest() {
    requestBuilder.sendSortHandByRunRequest();
  }
}

package view;

class ViewConstants {

  //Error-Messages
  static final String SERVER_NOT_AVAILABLE_ERROR = "Error!" +
          " Server is not available due to lack of host or not enough players in the game";
  static final String CONNECTION_REJECTED_ERROR = "The Host has rejected the connection." +
          " There might be no spot left in the game!";
  static final String MULTIPLE_HOSTS_ON_SINGLE_MACHINE_ERROR = "You are already hosting a Server!";
  static final String IP_NOT_DETERMINED_ERROR = "The IP address of a host could not be determined.";

  //FXML-Files
  static final String START_FXML = "start.fxml";
  static final String WAIT_FXML = "wait.fxml";
  static final String GAME_FXML = "game.fxml";
  static final String ERROR_FXML = "error.fxml";
  static final String WINNER_FXML = "winner.fxml";
  static final String HELP_FXML = "help.fxml";

  //Styling of text-fields
  static final String ERROR_STYLE = "-fx-border-color: red ; -fx-border-width: 2px ;";

  //Styling of the Player
  static final String CURRENTLY_PLAYING_STYLE = "-fx-border-color: white; -fx-border-width: 4px ;";
  static final String NOT_CURRENTLY_PLAYING_STYLE = "-fx-border-color: black; -fx-border-width: 4px ;";
  static final String NO_PLAYER_SYMBOL = "?";

  //Styling of the Stones
  static final String STONE_FORMAT = "stoneFormat";
  static final String STONE_STYLE = "-fx-background-color: none";
  static final String STONE_WHILE_DRAGGING_STYLE = "-fx-background-color: #FFFFFF44";
  static final String MULTIPLE_STONES_IMAGE = "images/MultiStoneDragView.png";
  static final String STONE_BACKGROUND_IMAGE = "images/StoneBackground.png";
  //Joker
  static final String JOKER_BACKGROUND_STYLE = "jokerBackground";
  static final String JOKER_SYMBOL = "J";

  //Cell-Styles
  static final String CELL_STYLE = "cell";
  static final String SHADOW_STYLE = "shadow";
  static final String STONE_VALUE_STYLE = "stoneValue";

  //ID's
  static final String HAND_GRID_ID = "handGrid";
  static final String TABLE_GRID_ID = "tableGrid";

  //Networking
  static final String LOCAL_IP = "localhost";
}

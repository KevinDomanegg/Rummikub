package view;

class ViewConstants {

  static final String SERVER_NOT_AVAILABLE_ERROR = "Error!" +
          " Server is not available due to lack of host or not enough players in the game";
  static final String CONNECTION_REJECTED_ERROR = "The Host has rejected the connection." +
          " There might be no spot left in the game!";
  static final String MULTIPLE_HOSTS_ON_SINGLE_MACHINE_ERROR = "You are already hosting a Server!";
  static final String IP_NOT_DETERMINED_ERROR = "The IP address of a host could not be determined.";

  static final String START_FXML = "start.fxml";
  static final String WAIT_FXML = "wait.fxml";
  static final String GAME_FXML = "game.fxml";
  static final String ERROR_FXML = "error.fxml";
  static final String WINNER_FXML = "winner.fxml";
  static final String HELP_FXML = "help.fxml";
}

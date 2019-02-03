package game;

/**
 * Class saving the history of moves a player has made during one turn.
 * Used to reset his moves if needed.
 */
class MoveTrace {
  enum Move {PUT_STONE, TABLE_MOVE}

  private String command;
  private int playerID;
  private Coordinate sourcePosition;
  private Coordinate targetPosition;

  MoveTrace(String command, int playerID, Coordinate sourcePosition, Coordinate targetPosition) {
    this.command = command;
    this.sourcePosition = sourcePosition;
    this.targetPosition = targetPosition;
    this.playerID = playerID;
  }

  MoveTrace(String command, Coordinate sourcePosition, Coordinate targetPosition) {
    this.command = command;
    this.sourcePosition = sourcePosition;
    this.targetPosition = targetPosition;
  }

  String getCommand() {
    return command;
  }

  Coordinate getInitialPosition() {
    return sourcePosition;
  }

  Coordinate getTargetPosition() {
    return targetPosition;
  }

  int getPlayerID() {
    return playerID;
  }

}

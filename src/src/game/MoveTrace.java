package game;

/**
 *  Move Data of a turn of the current Player
 *  which are important for reset to the Game before they played yet.
 */
final class MoveTrace {
  enum Move { PUT_STONE, TABLE_MOVE }

  private Move move;
  private Coordinate sourcePosition;
  private Coordinate targetPosition;

  MoveTrace(Move move, Coordinate sourcePosition, Coordinate targetPosition) {
    this.move = move;
    this.sourcePosition = sourcePosition;
    this.targetPosition = targetPosition;
  }

  Move getMove() {
    return move;
  }

  Coordinate getSourcePosition() {
    return sourcePosition;
  }

  Coordinate getTargetPosition() {
    return targetPosition;
  }
}

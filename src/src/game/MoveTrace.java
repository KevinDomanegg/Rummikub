package game;

/**
 * Class saving the history of moves a player has made during one turn.
 * Used to reset his moves if needed.
 */
class MoveTrace {
  enum Move {PUT_STONE, TABLE_MOVE}

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

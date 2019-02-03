package game;

/**
 * Interface defining abstract types of movements of Stones.
 */
interface StoneMove {
  void moveStone(Coordinate sourcePosition, Coordinate targetPosition);
}

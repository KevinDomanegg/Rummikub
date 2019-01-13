package communication.request;

import game.Coordinate;

abstract class AbstractMove {
  private Coordinate initialCoordinate;
  private Coordinate targetCoordinate;

  public AbstractMove(Coordinate initialCoordinate, Coordinate targetCoordinate) {
    this.initialCoordinate = initialCoordinate;
    this.targetCoordinate = targetCoordinate;
  }

  public Coordinate getInitialCoordinate() {
    return initialCoordinate;
  }

  public Coordinate getTargetCoordinate() {
    return targetCoordinate;
  }
}

package communication;

public enum RequestID {

  /**
   * Identifier for classes implementing the Request-Interface.
   * Every implementation of Request should have an RequestID associated with it.
   */

  HAND_MOVE,
  TABLE_MOVE,
  PUT_STONE,
  DRAW,
  CONFIRM_MOVE,
  GIVE_UP;

}

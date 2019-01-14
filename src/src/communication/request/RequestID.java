package communication.request;

public enum RequestID {

  /**
   * Identifier for classes implementing the Request-Interface.
   * Every implementation of Request should have an RequestID associated with it.
   */
  GET_PLAYERS_IDS,
  GET_HAND,
  GET_TABLE,
  SET_PLAYER,
  HAND_MOVE,
  TABLE_MOVE,
  PUT_STONE,
  DRAW,
  CONFIRM_MOVE,
  GIVE_UP
}

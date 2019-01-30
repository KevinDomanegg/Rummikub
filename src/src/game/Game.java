package game;

import java.util.List;
import java.util.Map;

public interface Game {

  boolean setPlayer(int playerID, String name, int age);

  boolean start();

  boolean moveSetOnTable(Coordinate sourcePosition, Coordinate targetPosition);

  void moveStoneOnTable(Coordinate sourcePosition, Coordinate targetPosition);

  boolean putSet(Coordinate sourcePosition, Coordinate targetPosition);

  boolean putStone(Coordinate sourcePosition, Coordinate targetPosition);

  boolean moveSetOnHand(int playerID, Coordinate sourcePosition, Coordinate targetPosition);

  void moveStoneOnHand(int playerID, Coordinate sourcePosition, Coordinate targetPosition);

  void drawStone();

  void kickPlayer(int playerID);

  void reset();

  void undo();

  boolean hasWinner();

  boolean isConsistent();

  Map<Coordinate, Stone> getTableStones();

  Map<Coordinate, Stone> getPlayerStones(int playerID);

  List<Integer> getPlayerHandSizes();

  int getBagSize();

  int getCurrentPlayerID();

  int getTableWidth();

  int getTableHeight();

  int getPlayerHandWidth(int playerID);

  int getPlayerHandHeight(int playerID);

  int getNumberOfPlayers();

  Map<Integer, Integer> getFinalRank();

  List<String> getPlayerNames();

  void sortPlayerHandByGroup(int playerID);

  void sortPlayerHandByRun(int playerID);

  boolean hasPlayerPlayedFirstMove(int playerID);

  // ? int getTime();
}

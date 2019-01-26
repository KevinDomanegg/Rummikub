package game;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public interface Game {

  void setPlayer(String name, int age);

  void start();

  void moveStoneOnTable(Coordinate initialPosition, Coordinate targetPosition);

  void putStone(Coordinate initialPosition, Coordinate targetPosition);

  void moveStoneOnHand(int playerID, Coordinate initialPosition, Coordinate targetPosition);

  void drawStone();

  void playerHasLeft(int playerPosition);

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

  List<Entry<Integer, Integer>> getFinalRank();

  List<String> getPlayerNames();

  void sortPlayerHandByGroup(int playerID);

  void sortPlayerHandByRun(int playerID);

  // ? int getTime();
}

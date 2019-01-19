package view;

import communication.gameinfo.StoneInfo;
import communication.request.SimpleRequest;

import java.util.List;

public class ClientModel {

  private StoneInfo[][] table;
  private StoneInfo[][] hand;
  private List<Integer> handSizes;
  private List<String> playersNames;
  private int BagSize;
  private boolean isMyTurn;
  private boolean isGameStarted;
  private int currentPlayerID;

  public void setHand(StoneInfo[][] newHand) {
    this.hand = newHand;
  }

  StoneInfo[][] getHand() {
    return hand;
  }

  public void setTable(StoneInfo[][] newTable) {
    this.table = newTable;
  }

  StoneInfo[][] getTable() {
    return table;
  }

  public void notifyInvalidMove() {
    // GIVE AN ERROR OR SOMETHING
  }

  void setBagSize(int bagSize) {
    this.BagSize = bagSize;
  }

  int getBagSize() {
    return getBagSize();
  }

  void setHandSizes(List<Integer> sizes) {
    this.handSizes = sizes;
  }

  List<Integer> getHandSizes() {
    return handSizes;
  }

  void setPlayerNames(List<String> names) {
    this.playersNames = names;
  }

  List<String> getPlayersNames() {
    return playersNames;
  }

  void setCurrentPlayer(int playerID) {
    currentPlayerID = playerID;
  }

  int getCurrentPlayerID() {
    return currentPlayerID;
  }

  void notifyTurn() {
    this.isMyTurn = true;
  }

  void finishTurn() {
    isMyTurn = false;
  }

  void notifyGameStart() {
    this.isGameStarted = true;
  }
}

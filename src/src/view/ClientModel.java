package view;

import communication.gameinfo.StoneInfo;

import java.util.List;

public class ClientModel {

    //These constants should - in a later stage - be declared globally
    //to guarantee consistency between client and server.
    //Therefore no getters are provided.

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

  public void setTable(StoneInfo[][] newTable) {
    this.table = newTable;
  }

  public void notifyInvalidMove() {
    // GIVE AN ERROR OR SOMETHING
  }

  void setBagSize(int bagSize) {
    this.BagSize = bagSize;
  }

  void setHandSizes(List<Integer> sizes) {
    this.handSizes = sizes;
  }

  void setPlayerNames(List<String> names) {
    this.playersNames = names;
  }

  void setCurrentPlayer(int playerID) {
    currentPlayerID = playerID;
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

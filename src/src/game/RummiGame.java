package game;


//This Class should be relabeled to Game, and the current Game interface needs to be moved.

public class RummiGame {
  private RummiHand[] hands;
  private RummiTable table;
  private RummiHand currentHand;


  public RummiGame(int currentHands, int[] ageList){
    //define starting player
    //define Order which player comes after each other
    //setOrder(ageList);

    //init hands
    //give Stones to hands
    //init table
  }

  //This method should define which player starts and who comes next
  private void setOrder(int[] ageList){

  }

  private void giveStonesToHands(){

  }

  public void moveStoneFromHand(Coordinate handStone, Coordinate tablePlace){
    Stone stoneOnHand = currentHand.getStones().get(handStone);

    table.setStone(tablePlace, stoneOnHand);
    currentHand.getStones().remove(handStone);
  }



}

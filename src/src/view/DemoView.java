package view;

import communication.gameinfo.StoneInfo;
import java.util.List;
import java.util.stream.Collectors;

public class DemoView {
  private static final String ANSI_RESET = "\u001B[0m";
  private static final String WHITE = "\u001B[37;40m";
  private static final String BLACK = "\u001B[47m";
  private static final String RED = "\u001B[41m";
  private static final String BLUE = "\u001B[31;44m";
  private static final String YELLOW = "\u001B[34;43m";
  private static final String JOKER = "\u001B[45m";

  private StoneInfo[][] table;
  private StoneInfo[][] hand;
  private int bagSize;
  private List<Integer> handSizes;
  private String name;

  public DemoView() {
  }

  public void setTable(StoneInfo[][] table) {
    this.table = table;
  }

  public void setPlayerHand(StoneInfo[][] hand) {
    this.hand = hand;
  }
  public void moveStoneOnTable(int initCol, int initRow, int targetCol, int targetRow) {
    table[targetCol][targetRow] = table[initCol][initRow];
    table[initCol][initRow] = null;
  }

  public void moveStoneFromHand(int initCol, int initRow, int targetCol, int targetRow) {
    table[targetCol][targetRow] = hand[initCol][initRow];
    hand[initCol][initRow] = null;
  }

  public void moveStoneOnHand(int initCol, int initRow, int targetCol, int targetRow) {
    hand[targetCol][targetRow] = hand[initCol][initRow];
    hand[initCol][initRow] = null;
  }

  public void printYourTurn(String name) {
    System.out.println(name + ": it is your turn.");
  }

  public void setBagSize(int bagSize) {
    this.bagSize = bagSize;
  }

  public void setHandSizes(List<Integer> handSizes) {
    this.handSizes = handSizes;
  }

  public void printGame() {
    StringBuilder stringBuilder = print(new StringBuilder(), table).append('\n')
        .append("Bag Size: ").append(bagSize).append('\n')
        .append(handSizes.stream().map(String::valueOf).collect(Collectors.joining(", ", "Hand Sizes: {", "}")))
        .append('\n');
    System.out.print(print(stringBuilder, hand));
  }

  private StringBuilder print(StringBuilder stringBuilder, StoneInfo[][] grid) {
    StoneInfo stoneInfo;
    int height = grid[0].length;
    for (int j = 0; j < height; j++) {
      for (StoneInfo[] row : grid) {
        stoneInfo = row[j];
        stringBuilder.append(parseColor(stoneInfo))
            .append((stoneInfo == null) ? 0 : Integer.toHexString(stoneInfo.getNumber()).toUpperCase())
            .append(ANSI_RESET);
      }
      stringBuilder.append('\n'); // for every row of the gird
    }
    return stringBuilder;
  }

  public void printNotYourTurn() {
    System.out.println("IT IS NOT YOUR TURN!");
  }

  public void printNotHost() {
    System.out.println("ONLY HOST CAN START THE GAME!");
  }

  private static String parseColor(StoneInfo stoneInfo) {
    if (stoneInfo == null) {
      return WHITE;
    }
    switch (stoneInfo.getColor()) {
      case "BLACK":
        return BLACK;
      case "YELLOW":
        return YELLOW;
      case "BLUE":
        return BLUE;
      case "RED":
        return RED;
      case "JOKER":
      default:
        return JOKER;
    }
  }

  public static void main(String[] args) {

  }

  public void printHostIP(String hostAddress) {
    System.out.println("Host Address: " + hostAddress);
  }

  public void printWrongMove() {
    System.out.println("WRONG MOVE!");
  }

  public void countDownBagSize() {
    bagSize--;
  }

  public void printGameAlreadyStarted() {
    System.out.println("GAME IS ALREADY STARTED");
  }
}

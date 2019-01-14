package view;

import game.Coordinate;
import game.Stone;
import java.util.Map;

public class DemoView {
  private static final String ANSI_RESET = "\u001B[0m";
  private static final String BLACK = "\u001B[47m";
  private static final String WHITE = "\u001B[37;40m";
  private static final String RED = "\u001B[41m";
  private static final String BLUE = "\u001B[31;44m";
  private static final String YELLOW = "\u001B[34;43m";
  private static final String JOKER = "\u001B[45m";

  private Map<Coordinate, Stone> table;
  private int tableWidth;
  private int tableHeight;
  private Map<Coordinate, Stone> playerHand;
  private int handWidth;
  private int handHeight;

  public DemoView() {
  }

  public void setTable(int tableWidth, int tableHeight, Map<Coordinate, Stone> table) {
    this.tableWidth = tableWidth;
    this.tableHeight = tableHeight;
    this.table = table;
  }

  public void setPlayerHand(int handWidth, int handHeight, Map<Coordinate, Stone> playerHand) {
    this.handWidth = handWidth;
    this.handHeight = handHeight;
    this.playerHand = playerHand;
  }

  public void printGame() {
    StringBuilder stringBuilder = print(new StringBuilder(),
        tableWidth, tableHeight, table).append('\n');
    System.out.print(print(stringBuilder, handWidth, handHeight, playerHand));
  }
  private StringBuilder print(StringBuilder stringBuilder, int width, int height, Map<Coordinate, Stone> stones) {
    Stone stone;

    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {
        stone = stones.get(new Coordinate(i, j));
        // builder = stone-color + stone-number + ANSI_RESET
        stringBuilder.append(parseColor(stone));
        stringBuilder.append((stone == null) ? 0 : Integer.toHexString(stone.getNumber()).toUpperCase())
            .append(ANSI_RESET);
      }
      stringBuilder.append('\n'); // for every row of the gird
    }
    return stringBuilder;
  }

  private static String parseColor(Stone stone) {
    if (stone == null) {
      return WHITE;
    }
    switch (stone.getColor()) {
      case BLACK:
        return BLACK;
      case YELLOW:
        return YELLOW;
      case BLUE:
        return BLUE;
      case RED:
        return RED;
      default:
        return JOKER;
    }
  }
}

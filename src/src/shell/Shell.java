package shell;

import game.Coordinate;
import game.RummiTable;
import game.Stone;
import game.Stone.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class Shell {
  private Shell() {}

  private static final String ANSI_RESET = "\u001B[0m";
  private static final String BLACK = "\u001B[47m";
  private static final String WHITE = "\u001B[37;40m";
  private static final String RED = "\u001B[41m";
  private static final String BLUE = "\u001B[31;44m";
  private static final String YELLOW = "\u001B[34;43m";
  private static final String JOKER = "\u001B[45m";

  private static final String HELP = "Commands: \n"
      + "  new:\n"
      + "    make a new table\n"
      + "  print:\n"
      + "    print the table\n"
      + "  stone <col> <row> <color> <number>:\n"
      + "    set a stone with <color> and <number> at (<col>, <row>)\n"
      + "  check:\n"
      + "    check the consistency of the table and return the result\n"
      + "  help:\n"
      + "    print help text";

  private static final String PROMPT = "rummikub> ";
  private static String[] tokens;
  private static RummiTable table;


  public static void main(String[] args) throws IOException {
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in, StandardCharsets.UTF_8));
    start(reader);
    reader.close();
  }

  private static void start(BufferedReader reader) throws IOException {
    String input;
    System.out.println(HELP);

    while (true) {
      System.out.print(PROMPT);
      input = reader.readLine();
      if (input == null) {
        break;
      }
      tokens = input.trim().split("\\s+");
      if (tokens[0].equals("quit")) {
        break;
      }
      excecute(tokens);
    }

  }

  private static void excecute(String[] tokens) {
    try {
      switch (tokens[0]) {
        case "new":
          table = new RummiTable();
          break;
        case "stone":
          setStone();
          break;
        case "print":
          print();
          break;
        case "check":
          System.out.println(table.isConsistent());
          break;
        case "help":
          System.out.println(HELP);
          break;
        default:
          System.out.println("error");
          break;
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void setStone() {
    table.setStone(new Coordinate(Integer.parseUnsignedInt(tokens[1]), Integer.parseUnsignedInt(tokens[2])), parseStone(tokens[3], tokens[4]));
  }

  private static void clear() {
    table.clear();
  }

  private static void print() {
    StringBuilder builder = new StringBuilder();
    Map<Coordinate, Stone> stones = table.getStones();
    Stone stone;

    for (int j = 0; j < 5; j++) {
      for (int i = 0; i < table.getWidth(); i++) {
        stone = stones.get(new Coordinate(i, j));
        // builder = stone-color + stone-number + ANSI_RESET
        builder.append(parseColor(stone));
        builder.append((stone == null) ? 0 : stone.getNumber())
            .append(ANSI_RESET);
      }
      builder.append('\n'); // for every row of the gird
    }
    System.out.print(builder.toString());
  }

  private static Stone parseStone(String color, String number) {
    color = color.toUpperCase();
    return (color.equals("JOKER")) ? new Stone() : new Stone(Color.valueOf(color), Integer.parseUnsignedInt(number));
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

package shell;

import game.Coordinate;
import game.Stone;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import network.client.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import view.DemoView;

public final class Shell {
  private Shell() {}

  private static final String HELP_TEXT_FOR_SHELL = "Welcome to Rummikub_DEMO Version, powered by CurryGang, \n" +
      "Here is a TEST *NON-GUI* Version of our program. \n" +
      "Commands: \n" +
      "HOST <username::String> <age::Integer> <number_of_clients_in_game::int>: With this command you host a game \n" +
      "and wait for you opponent to connect so you can start the game. \n" +
      "JOIN <username::String> <age::Integer> <IP_Address_from_Server::String>: You join a Server with already an opponent \n" +
      "START: Send a request_to_start_game to the Server and the Server starts the Game \n" +
      "PRINT: Prints the Table of the Game and your hand (with colors) \n" +
      "MOVE_ON_TABLE <Initial_column::Integer> <Initial_row::Integer> <Target_column::Integer> <Target_row::Integer>  \n" +
      "MOVE_FROM_HAND <Initial_column_in_hand::Integer> <Initial_row_in_hand::Integer> <Target_column_in_table::Integer> <Target_row_in_table::Integer> \n" +
      "CHECK: Checks the consistent of the stones that you just puted on the table. \n" +
      "HELP: Prints a message that helps you understand how the Shell of the program works. \n" +
      "QUIT: Terminates the program.";

  enum Command {
    HOST {
      @Override Controller execute(Controller controller, String[] tokens) {
        controller.host(tokens[1], Integer.parseUnsignedInt(tokens[2]));
        try {
          System.out.println(Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
          error(e.getMessage());
        }
        return controller;
      }
    },
    JOIN {
      @Override Controller execute(Controller controller, String[] tokens) {
        controller.join(tokens[1], Integer.parseUnsignedInt(tokens[2]), tokens[3]);
        return controller;
      }
    },
    START {
      @Override Controller execute(Controller controller, String[] tokens) {
        controller.startGame();
        return controller;
      }

    },
    PRINT {
      @Override Controller execute(Controller controller, String[] tokens) {
        controller.printGame();
        return controller;
      }
    },
    CHECK {
      @Override Controller execute(Controller controller, String[] tokens) {
        controller.sendCheck();
        return controller;
      }
    },
    DRAW {
      @Override Controller execute(Controller controller, String[] tokens) {
        return controller;
      }
    },
    MOVE_ON_TABLE {
      @Override Controller execute(Controller controller, String[] tokens) {
        int initCol = Integer.parseUnsignedInt(tokens[1]);
        int initRow = Integer.parseUnsignedInt(tokens[2]);
        int targetCol = Integer.parseUnsignedInt(tokens[3]);
        int targetRow = Integer.parseUnsignedInt(tokens[4]);
        controller.moveStoneOnTable(initCol, initRow, targetCol, targetRow);
        return controller;
      }
    },
    MOVE_FROM_HAND {
      @Override Controller execute(Controller controller, String[] tokens) {
        int initCol = Integer.parseUnsignedInt(tokens[1]);
        int initRow = Integer.parseUnsignedInt(tokens[2]);
        int targetCol = Integer.parseUnsignedInt(tokens[3]);
        int targetRow = Integer.parseUnsignedInt(tokens[4]);
        controller.moveStoneFromHand(initCol, initRow, targetCol, targetRow);
        return controller;
      }
    },
    MOVE_ON_HAND {
      @Override
      Controller execute(Controller controller, String[] tokens) {
        int initCol = Integer.parseUnsignedInt(tokens[1]);
        int initRow = Integer.parseUnsignedInt(tokens[2]);
        int targetCol = Integer.parseUnsignedInt(tokens[3]);
        int targetRow = Integer.parseUnsignedInt(tokens[4]);
        controller.moveStoneOnHand(initCol, initRow, targetCol, targetRow);
        return controller;

      }
    },
    PLAYER_LEFT {
      @Override
      Controller execute(Controller controller, String[] tokens) {
        return controller;
      }
    },
    UNDO {
      @Override Controller execute(Controller controller, String[] tokens) {
        return controller;
      }
    },
    HELP {
      @Override
      Controller execute(Controller controller, String[] tokens) {
        System.out.println(HELP_TEXT_FOR_SHELL);
        return controller;
      }
    },
    QUIT {
      @Override
      Controller execute(Controller controller, String[] tokens) {
        return controller;
      }
    },
    NO_COMMAND {
      @Override Controller execute(Controller controller, String[] tokens) {
        error("no command");
        return controller;
      }
    },
    NOT_VALID;

    Controller execute(Controller controller, String[] tokens) {
      return controller;
    }
  }

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

  private static final String MISSING_OR_TOO_MUCH_TOKENS_FOR = "missing or too much tokens";
  
  private static final String PROMPT = "rummikub> ";

  public static void main(String[] args) throws IOException {
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in, StandardCharsets.UTF_8));
    System.out.println("Welcome to Rummikub_DEMO Version, powered by CurryGang !");
    start(reader);
    reader.close();
  }

  private static void start(BufferedReader reader) throws IOException {
    String input;
    Command command;
    String[] tokens;
    Controller controller = new Controller(new DemoView());

    while (true) {
      System.out.print(PROMPT);
      input = reader.readLine();
      if (input == null) {
        break;
      }
      tokens = input.trim().split("\\s+");
      command = parseCommand(tokens);
      if (command == Command.QUIT) {
        break;
      }
      try {
        controller = command.execute(controller, tokens);
      } catch (Exception e ) {
        error(e.getMessage());
      }
    }

  }

  private static Command parseCommand(String[] tokens) {
    switch (tokens[0].toUpperCase()) {
      case "HOST":
        return Command.HOST;
      case "JOIN":
        return Command.JOIN;
      case "":
        return Command.NO_COMMAND;
      //case "NEW":
        //return parseCommandNEW(tokens);
      case "START":
        return Command.START;
      case "MOVE_FROM_HAND":
        return Command.MOVE_FROM_HAND;
      case "MOVE_ON_TABLE":
        return Command.MOVE_ON_TABLE;
      case "MOVE_ON_HAND":
        return Command.MOVE_ON_HAND;
      case "PRINT":
      return Command.PRINT;
      case "CHECK":
      return Command.CHECK;
      case "HELP":
      return Command.HELP;
      case "DRAW":
      return Command.DRAW;
      case "QUIT":
      return Command.QUIT;
      case "UNDO":
      return Command.UNDO;
      default:
        error(Command.NOT_VALID.toString());
        return Command.NOT_VALID;
    }
    
  }

  /*private static Command parseCommandNEW(String[] tokens) {
    if (tokens.length > 2) {
      error(MISSING_OR_TOO_MUCH_TOKENS_FOR + Command.NEW);
      return Command.NOT_VALID;
    }
    return Command.NEW;
  }*/


  private static void print(int tableWidth, int tableHeight, Map<Coordinate, Stone> tableStones,
                            int handWidth, int handHeight, Map<Coordinate, Stone> handStones) {
    StringBuilder stringBuilder = printGame(tableWidth, tableHeight,
        new StringBuilder(), tableStones).append('\n');
    System.out.print(printGame(handWidth, handHeight,
        stringBuilder, handStones));
  }
  private static StringBuilder printGame(int width, int height, StringBuilder stringBuilder, Map<Coordinate, Stone> stones) {
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

  private static void error(String message) {
    System.out.println("Error! " + message);
  }
}

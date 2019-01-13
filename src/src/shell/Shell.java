package shell;

import communication.gameinfo.GameInfo;
import communication.gameinfo.HandInfo;
import communication.gameinfo.TableInfo;
import communication.request.GetHand;
import communication.request.GetTable;
import communication.request.Request;
import game.Coordinate;
import game.Game;
import game.RummiGame;
import game.Stone;
import network.client.RummiClient;
import network.server.RummiServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class Shell {
  private Shell() {}


  enum Command {
    HOST {
      @Override RummiClient excecute(RummiClient client, String[] tokens) {
          new RummiServer(Integer.parseUnsignedInt(tokens[3])).start();
          client = new RummiClient(tokens[1], Integer.parseUnsignedInt(tokens[2]), "localhost");
          client.start();
          return client;
      }
    },
    JOIN {
      @Override RummiClient excecute(RummiClient client, String[] tokens) {
        client = new RummiClient(tokens[1], Integer.parseUnsignedInt(tokens[2]), tokens[3]);
        client.start();
        return client;
      }
    },
    PRINT {
      @Override RummiClient excecute(RummiClient client, String[] tokens) {
        GameInfo gameinfo_table;
        GameInfo gameinfo_hand;
        client.setSendToServer(new GetTable());
        gameinfo_table = client.getTableInfo();
        client.setSendToServer(new GetHand());
        gameinfo_hand = client.getTableInfo();
        Map<Coordinate, Stone> handstones = client.applyGameInfoHandler(gameinfo_hand);
        Map<Coordinate, Stone> tablestones = client.applyGameInfoHandler(gameinfo_table);
        print(((TableInfo) (gameinfo_table)).getWidth(), ((TableInfo) (client.getTableInfo())).getHeight(),tablestones,
            ((HandInfo) (gameinfo_hand)).getWidth(), ((HandInfo) (gameinfo_hand)).getHeight(), handstones);
        return client;
      }
    },
    /*
    CHECK {
      @Override RummiClient excecute(RummiClient game, String[] tokens) {
        System.out.println(game.isConsistent());
        return game;
      }
    },
    DRAW {
      @Override RummiClient excecute(RummiClient game, String[] tokens) {
        game.drawStone();
        return game;
      }
    },
    MOVE_ON_TABLE {
      @Override RummiClient excecute(RummiClient game, String[] tokens) {
        int x1 = Integer.parseUnsignedInt(tokens[1]);
        int y1 = Integer.parseUnsignedInt(tokens[2]);
        int x2 = Integer.parseUnsignedInt(tokens[3]);
        int y2 = Integer.parseUnsignedInt(tokens[4]);
        game.moveStoneOnTable(new Coordinate(x1, y1), new Coordinate(x2, y2));
        return game;
      }
    },
    MOVE_FROM_HAND {
      @Override RummiClient excecute(RummiClient game, String[] tokens) {
        int x1 = Integer.parseUnsignedInt(tokens[1]);
        int y1 = Integer.parseUnsignedInt(tokens[2]);
        int x2 = Integer.parseUnsignedInt(tokens[3]);
        int y2 = Integer.parseUnsignedInt(tokens[4]);
        game.moveStoneFromHand(new Coordinate(x1, y1), new Coordinate(x2, y2));
        return game;
      }
    },
    MOVE_ON_HAND {
      @Override
      RummiClient excecute(RummiClient game, String[] tokens) {
        int x1 = Integer.parseUnsignedInt(tokens[1]);
        int y1 = Integer.parseUnsignedInt(tokens[2]);
        int x2 = Integer.parseUnsignedInt(tokens[3]);
        int y2 = Integer.parseUnsignedInt(tokens[4]);
        game.moveStoneOnHand(game.getCurrentPlayerPosition(), new Coordinate(x1, y1), new Coordinate(x2, y2));
        return game;

      }
    },
    PLAYER_LEFT {
      @Override
      RummiClient excecute(RummiClient game, String[] tokens) {
        return null;
      }
    },
    UNDO {
      @Override RummiClient excecute(RummiClient game, String[] tokens) {
        game.undo();
        return game;
      }
    },
    HELP {
      @Override
      RummiClient excecute(RummiClient game, String[] tokens) {
        System.out.println(HELP);
        return game;
      }
    },
    QUIT {
      @Override
      RummiClient excecute(RummiClient game, String[] tokens) {
        return null;
      }
    },*/
    NO_COMMAND {
      @Override RummiClient excecute(RummiClient game, String[] tokens) {
        error("no command");
        return game;
      }
    },
    NOT_VALID;

    RummiClient excecute(RummiClient client, String[] tokens) {
      return client;
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
    start(reader);
    reader.close();
  }

  private static void start(BufferedReader reader) throws IOException {
    String input;
    Command command;
    String[] tokens;
    RummiClient client = null;

    while (true) {
      System.out.print(PROMPT);
      input = reader.readLine();
      if (input == null) {
        break;
      }
      tokens = input.trim().split("\\s+");
      command = parseCommand(tokens);
      /*if (command == Command.QUIT) {
        break;
      }*/
      try {
        client = command.excecute(client, tokens);
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
      //case "PLAYER":
        //return Command.PLAYER;
      case "PRINT":
        return Command.PRINT;
      /*case "CHECK":
        return Command.CHECK;
      case "HELP":
        return Command.HELP;
      case "MOVE_FROM_HAND":
        return Command.MOVE_FROM_HAND;
      case "MOVE_ON_TABLE":
        return Command.MOVE_ON_TABLE;
      case "DRAW":
        return Command.DRAW;
      case "START":
        return Command.START;
      case "QUIT":
        return Command.QUIT;
      case "UNDO":
        return Command.UNDO;
      case "MOVE_ON_HAND":
        return Command.MOVE_ON_HAND;*/
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

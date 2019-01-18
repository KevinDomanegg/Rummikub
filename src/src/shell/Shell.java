package shell;

import network.client.ShellController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import view.DemoView;

public final class Shell {
  private Shell() {}

  private static final String HELP_TEXT_FOR_SHELL = "Welcome to Rummikub_DEMO Version, powered by CurryGang, \n" +
      "Here is a TEST *NON-GUI* Version of our program. \n" +
      "PLEASE note that error messages are not reliable. Here is how you play our Demo:\n\n" +
      "1. host a game by: host <name> <age>\n" +
      "2. open another shell or with another computer join the game by: join <name> <age> <serverIP>" +
      "3. host starts the game by: start\n" +
      "4. print table and hand by: print\n" +
      "rest you can see the followings...\n\n" +
      "Commands: \n" +
      "   HOST <username::String> <age::Integer> <number_of_clients_in_game::int>: With this command you host a game \n" +
      "   and wait for you opponent to connect so you can start the game. \n" +
      "   JOIN <username::String> <age::Integer> <IP_Address_from_Server::String>: You join a Server with already an opponent \n" +
      "   START: Send a request_to_start_game to the Server and the Server starts the Game \n" +
      "   PRINT: Prints the Table of the Game and your hand (with colors) \n" +
      "   MOVE_ON_TABLE <Initial_column::Integer> <Initial_row::Integer> <Target_column::Integer> <Target_row::Integer>  \n" +
      "   PUT_STONE <Initial_column_in_hand::Integer> <Initial_row_in_hand::Integer> <Target_column_in_table::Integer> <Target_row_in_table::Integer> \n" +
      "   CHECK: Checks the consistent of the stones that you just puted on the table. \n" +
      "   HELP: Prints a message that helps you understand how the Shell of the program works. \n" +
      "   QUIT: Terminates the program.";

  enum Command {
    HOST {
      @Override
      ShellController execute(ShellController shellController, String[] tokens) {
        shellController.host(tokens[1], Integer.parseUnsignedInt(tokens[2]));
        return shellController;
      }
    },
    JOIN {
      @Override
      ShellController execute(ShellController shellController, String[] tokens) {
        shellController.join(tokens[1], Integer.parseUnsignedInt(tokens[2]), tokens[3]);
        return shellController;
      }
    },
    START {
      @Override
      ShellController execute(ShellController shellController, String[] tokens) {
        shellController.startGame();
        return shellController;
      }

    },
    PRINT {
      @Override
      ShellController execute(ShellController shellController, String[] tokens) {
        shellController.printGame();
        return shellController;
      }
    },
    CHECK {
      @Override
      ShellController execute(ShellController shellController, String[] tokens) {
        shellController.sendCheck();
        return shellController;
      }
    },
    DRAW {
      @Override
      ShellController execute(ShellController shellController, String[] tokens) {
        shellController.draw();
        return shellController;
      }
    },
    MOVE_ON_TABLE {
      @Override
      ShellController execute(ShellController shellController, String[] tokens) {
        int initCol = Integer.parseUnsignedInt(tokens[1]);
        int initRow = Integer.parseUnsignedInt(tokens[2]);
        int targetCol = Integer.parseUnsignedInt(tokens[3]);
        int targetRow = Integer.parseUnsignedInt(tokens[4]);
        shellController.moveStoneOnTable(initCol, initRow, targetCol, targetRow);
        return shellController;
      }
    },
    PUT_STONE {
      @Override
      ShellController execute(ShellController shellController, String[] tokens) {
        int initCol = Integer.parseUnsignedInt(tokens[1]);
        int initRow = Integer.parseUnsignedInt(tokens[2]);
        int targetCol = Integer.parseUnsignedInt(tokens[3]);
        int targetRow = Integer.parseUnsignedInt(tokens[4]);
        shellController.moveStoneFromHand(initCol, initRow, targetCol, targetRow);
        return shellController;
      }
    },
    MOVE_ON_HAND {
      @Override
      ShellController execute(ShellController shellController, String[] tokens) {
        int initCol = Integer.parseUnsignedInt(tokens[1]);
        int initRow = Integer.parseUnsignedInt(tokens[2]);
        int targetCol = Integer.parseUnsignedInt(tokens[3]);
        int targetRow = Integer.parseUnsignedInt(tokens[4]);
        shellController.moveStoneOnHand(initCol, initRow, targetCol, targetRow);
        return shellController;

      }
    },
    UNDO {
      @Override
      ShellController execute(ShellController shellController, String[] tokens) {
        return shellController;
      }
    },
    HELP {
      @Override
      ShellController execute(ShellController shellController, String[] tokens) {
        System.out.println(HELP_TEXT_FOR_SHELL);
        return shellController;
      }
    },
    NO_COMMAND {
      @Override
      ShellController execute(ShellController shellController, String[] tokens) {
        error("no command");
        return shellController;
      }
    },
    RESET {
      @Override
      ShellController execute(ShellController shellController, String[] tokens) {
        shellController.reset();
        return shellController;
      }
    },
    QUIT,
    NOT_VALID;

    ShellController execute(ShellController shellController, String[] tokens) {
      return shellController;
    }
  }
  
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
    ShellController shellController = new ShellController(new DemoView());
    System.out.println(HELP_TEXT_FOR_SHELL);

    while (true) {
      System.out.print(PROMPT);
      input = reader.readLine();
      if (input == null) {
        break;
      }
      tokens = input.trim().split("\\s+");
      command = parseCommand(tokens);
      if (command == Command.QUIT) {
        shellController.disconnectClient();
        break;
      }
      try {
        shellController = command.execute(shellController, tokens);
      } catch (Exception e ) {
        error(e.getMessage());
      }
    }

  }

  private static Command parseCommand(String[] tokens) {
    switch (tokens[0].toUpperCase()) {
      case "":
        return Command.NO_COMMAND;
      case "HOST":
      return Command.HOST;
      case "JOIN":
      return Command.JOIN;
      case "START":
        return Command.START;
      case "PUT_STONE":
        return Command.PUT_STONE;
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
      case "QUIT":
      return Command.QUIT;
      case "UNDO":
      return Command.UNDO;
      case "DRAW":
        return Command.DRAW;
      case "RESET":
        return Command.RESET;
      default:
        error(Command.NOT_VALID.toString());
        return Command.NOT_VALID;
    }
    
  }

  private static void error(String message) {
    System.out.println("Error! " + message);
  }
}

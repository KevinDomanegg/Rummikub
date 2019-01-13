package game;

public class MoveTrace {
  private String command;
  private int playerPosition;
  private Coordinate initialPosition;
  private Coordinate targetPosition;

  public MoveTrace(String command, int playerPosition, Coordinate initialPosition, Coordinate targetPosition) {
    this.command = command;
    this.initialPosition = initialPosition;
    this.targetPosition = targetPosition;
    this.playerPosition = playerPosition;
  }

  public MoveTrace(String command, Coordinate initialPosition, Coordinate targetPosition) {
    this.command = command;
    this.initialPosition = initialPosition;
    this.targetPosition = targetPosition;
  }


  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public Coordinate getInitialPosition() {
    return initialPosition;
  }

  public void setInitialPosition(Coordinate initialPosition) {
    this.initialPosition = initialPosition;
  }

  public Coordinate getTargetPosition() {
    return targetPosition;
  }

  public void setTargetPosition(Coordinate targetPosition) {
    this.targetPosition = targetPosition;
  }

  public int getPlayerPosition() {
    return playerPosition;
  }

  public void setPlayerPosition(int playerPosition) {
    this.playerPosition = playerPosition;
  }




}

package view;

import communication.gameinfo.StoneInfo;

public class ClientModel {

    //These constants should - in a later stage - be declared globally
    //to guarantee consistency between client and server.
    //Therefore no getters are provided.
    private static final int tableHeight = 20;
    private static final int tableWidth = 5;
    private static final int handHeight = 2;
    private static final int handWidth = 20;

    private ClientPlayer[] opponents;
    private StoneInfo[][] hand;
    private StoneInfo[][] table;
    private int BagSize;
    private String name;
    private int age;
    private ClientPlayer currentPlayer;
    private boolean myTurn;


    public ClientModel(){
        this.opponents = new ClientPlayer[3];
        this.hand = new StoneInfo[handWidth][handHeight];
        this.table = new StoneInfo[tableWidth][tableHeight];
        this.myTurn = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHand(StoneInfo[][] newHand) {
        this.hand = newHand;
    }

    public void setTable(StoneInfo[][] newTable) {
        this.table = newTable;
    }

    public void setCurrentPlayer(int opponentID) {
        this.currentPlayer = opponents[opponentID];
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public void setOpponents(ClientPlayer[] opponents) {
        this.opponents = opponents;
    }

    public int getStoneCount(int opponentID) {
        return opponents[opponentID].getStoneCount();
    }

    /**
     * Returns the name of a specific opponent.
     *
     * @param opponentID 0-2; Identifier of the specific opponent
     * @return String representing the opponent's name
     * @throws IllegalArgumentException the specified coordinates are not part
     *                                  of the table
     */
    public String getOpponentName(int opponentID) throws IllegalArgumentException{
        if (opponentID < 0 || opponentID >= opponents.length) {
            throw new IllegalArgumentException();
        }
        return opponents[opponentID].getName();
    }

    /**
     * Returns the number of Stones a specific opponent has on his hand.
     *
     * @param opponentID 0-2; Identifier of the specific opponent
     * @return int representing the opponent's stone-count
     * @throws IllegalArgumentException the specified coordinates are not part
     *                                  of the table
     */
    public int getOpponentStoneCount(int opponentID) throws IllegalArgumentException{
        if (opponentID < 0 || opponentID >= opponents.length) {
            throw new IllegalArgumentException();
        }
        return opponents[opponentID].getStoneCount();
    }

    /**
     * Returns the age of a specific opponent.
     *
     * @param opponentID 0-2; Identifier of the specific opponent
     * @return int representing the opponent's age
     * @throws IllegalArgumentException the specified coordinates are not part
     *                                  of the table
     */
    public int getOpponentAge(int opponentID) throws IllegalArgumentException{
        if (opponentID < 0 || opponentID >= opponents.length) {
            throw new IllegalArgumentException();
        }
        return opponents[opponentID].getAge();
    }

    /**
     * Returns the color of a specific Stone on the player's hand.
     *
     * @param x x-coordinate of the Stone
     * @param y y-coordinate of the Stone
     * @return String representing the color of the Stone
     * @throws IllegalArgumentException the specified coordinates are not part
     *                                  of the table
     */
    public String getHandStoneColor(int x, int y) throws IllegalArgumentException {
        boolean xOutOfBounds = x < 0 || x > handWidth - 1;
        boolean yOutOfBounds = y < 0 || y > handHeight - 1;
        if (xOutOfBounds || yOutOfBounds) {
            throw new IllegalArgumentException();
        }

        return hand[x][y].getColor();
    }

    /**
     * Returns the number of a specific Stone on the player's hand.
     *
     * @param x x-coordinate of the Stone
     * @param y y-coordinate of the Stone
     * @return int representing the number of the Stone
     * @throws IllegalArgumentException the specified coordinates are not part
     *                                  of the table
     */
    public int getHandStoneNumber(int x, int y) throws IllegalArgumentException {
        boolean xOutOfBounds = x < 0 || x > handWidth - 1;
        boolean yOutOfBounds = y < 0 || y > handHeight - 1;
        if (xOutOfBounds || yOutOfBounds) {
            throw new IllegalArgumentException();
        }

        return hand[x][y].getNumber();
    }

    /**
     * Returns the color of a specific Stone on the table.
     *
     * @param x x-coordinate of the Stone
     * @param y y-coordinate of the Stone
     * @return String representing the color of the Stone
     * @throws IllegalArgumentException the specified coordinates are not part
     *                                  of the table
     */
    public String getTableStoneColor(int x, int y) throws IllegalArgumentException {
        boolean xOutOfBounds = x < 0 || x > tableWidth - 1;
        boolean yOutOfBounds = y < 0 || y > handHeight - 1;
        if (xOutOfBounds || yOutOfBounds) {
            throw new IllegalArgumentException();
        }

        return table[x][y].getColor();
    }

    /**
     * Returns the number of a specific Stone on the table.
     *
     * @param x x-coordinate of the Stone
     * @param y y-coordinate of the Stone
     * @return int representing the number of the Stone
     * @throws IllegalArgumentException the specified coordinates are not part
     *                                  of the table
     */
    public int getTableStoneNumber(int x, int y) throws IllegalArgumentException {

        boolean xOutOfBounds = x < 0 || x > tableWidth - 1;
        boolean yOutOfBounds = y < 0 || y > tableHeight - 1;
        if (xOutOfBounds || yOutOfBounds) {
            throw new IllegalArgumentException();
        }

        return table[x][y].getNumber();
    }
}

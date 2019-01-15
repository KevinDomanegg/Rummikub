package src.view;

public class GameModel {

    private String firstPlayerName;
    private String secondPlayerName;
    private String thirdPlayerName;
    private String [] [] playerHand; // The Array will be from Type StoneInfo

    private void setFirstPlayerName(String name) {
        firstPlayerName = name;
    }

    private void setSecondPlayerName(String name) {
        secondPlayerName = name;
    }

    private void setThirdPlayerName(String name) {
        thirdPlayerName = name;
    }

    private String getFirstPlayerName() {
        return firstPlayerName;
    }

    private String getSecondPlayerName() {
        return secondPlayerName;
    }

    private String getThirdPlayerName() {
        return thirdPlayerName;
    }

    private void setPlayerHand(String [] [] hand) {
        playerHand = playerHand;
    }


}

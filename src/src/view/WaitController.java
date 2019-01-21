package view;

import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.client.RequestBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WaitController implements Initializable {

  private RequestBuilder requestBuilder;
  private NetworkController networkController;

  private ClientModel model;

  @FXML
  private Label waitingState;

  @FXML
  private Text ipAddress;

  @FXML
  private Button startGameButton;

  @FXML
  private Text player0;

  @FXML
  private Text player1;

  @FXML
  private Text player2;

  @FXML
  private Text player3;

  @FXML
  private void startGame() {
    requestBuilder.sendStartRequest();
  }

  void setNetworkController(NetworkController networkController) {
    this.networkController = networkController;
  }

  void setRequestBuilder(RequestBuilder requestBuilder) {
    this.requestBuilder = requestBuilder;
  }

  void returnToStartView() {
    networkController.returnToStartView();
  }

 void setPlayerNames(List<String> names) {
   int size = names.size();
   switch (size) {
     case 4:
       player3.setText(names.get(3));
     case 3:
       player2.setText(names.get(2));
     case 2:
       player1.setText(names.get(1));
     case 1:
       player0.setText(names.get(0));
     default:
   }
   if (model.isHost()) {
      if (size > 1) {
        // start button visible
        startGameButton.setVisible(true);
        return;
      }
   }
   // start button not visible
   startGameButton.setVisible(false);
 }

 synchronized void switchToGameView() {
    synchronized (networkController) {
      Stage stage = (Stage) startGameButton.getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
      Parent root = loader.getRoot();
      try {
        root = loader.load();
      } catch (IOException e) {
        e.printStackTrace();
      }

      GameController gameController = loader.getController();
      gameController.setModel(model);

      gameController.setRequestBuilder(requestBuilder);
      gameController.setNetworkController(networkController);
      networkController.setGameController(gameController);

      Scene gameScene = new Scene(root, 1024, 768);
      gameScene.getStylesheets().add("view/gameStyle.css");
      stage.setScene(gameScene);
      notifyAll();
      //System.out.println("switched to game");
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //ipAddress.setText();
  }

  void setModel(ClientModel model) {
    this.model = model;
    if (model.isHost()) {
      waitingState.setText("hosting Game");
    }
    ipAddress.setText(model.getServerIP());
  }
}

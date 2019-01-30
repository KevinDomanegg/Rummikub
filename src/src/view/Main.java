package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.*;
import javafx.stage.Stage;
//MUSIC
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.net.URISyntaxException;


public class Main extends Application {

  private static final String GAME_NAME = "Rummikub";
  //private Media sound = new Media(new File("C:\\Users\\Angelos Kafounis\\Desktop\\rummikub---currygang\\src\\src\\view\\startMusic.mp3").toURI().toString());
//  private Media sound;
//  private Media video;
//  {
//    try{
//      //video = new Media((getClass().getResource("animeTestVideo.mp4")).toURI().toString());
//    }catch(Exception e) {
//    }
//
//      try {
//     sound = new Media((getClass().getResource("startMusic.mp3")).toURI().toString());
//    } catch (URISyntaxException e) {
//      e.printStackTrace();
//    }
//  }
//  private MediaPlayer mediaPlayer_video= new MediaPlayer( new Media(getClass().getResource("entranceVideo3.mp4").toExternalForm()));
//  MediaView mediaView = new MediaView(mediaPlayer_video);

//  private MediaPlayer mediaPlayer = new MediaPlayer(sound);

  //WaitController waitController;
  GameController gameController;

  public static void main(String[] args) {
    launch(args);
  }

  public void hostJoinStage(Stage primaryStage) throws Exception {
    //FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
    FXMLLoader loader = new FXMLLoader(getClass().getResource("start.fxml"));
    Parent root = loader.load();
    //gameController = loader.getController();
    StartController startController = loader.getController();
//    startController.setMain(this);


    // A Little Music
    //mediaPlayer.play();

    primaryStage.setTitle("Rummikub");
    Scene scene = new Scene(root, 1500, 900);
    primaryStage.setScene(scene);
    // MINIMUM WINDOW SIZE
    primaryStage.setMinHeight(650.0); //TODO: Do we need this? And why it's 650X600?
    primaryStage.setMinWidth(600.0);
    primaryStage.show();
    //mediaPlayer_video.play();
//    startController.setMain(this);
  }

  //TODO: Catch exception
  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle(GAME_NAME);
    primaryStage.setMinHeight(650.0);
    primaryStage.setMinWidth(600.0);
    new MainController(primaryStage).switchToStartScene();
  }

  void stopMusic() {
    //this.mediaPlayer.stop();
  }
}

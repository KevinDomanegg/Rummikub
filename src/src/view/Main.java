package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.*;
import javafx.stage.Stage;
//MUSIC
import javafx.scene.media.MediaPlayer;
import java.io.File;

import java.awt.*;
import java.net.URISyntaxException;


public class Main extends Application {
  private StartController startController;

  //private Media sound = new Media(new File("C:\\Users\\Angelos Kafounis\\Desktop\\rummikub---currygang\\src\\src\\view\\startMusic.mp3").toURI().toString());
  private Media sound;
  private Media video;
  {
    try{
      //video = new Media((getClass().getResource("animeTestVideo.mp4")).toURI().toString());
    }catch(Exception e) {
    }

      try {
     sound = new Media((getClass().getResource("startMusic.mp3")).toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }
  private MediaPlayer mediaPlayer_video= new MediaPlayer( new Media(getClass().getResource("entranceVideo3.mp4").toExternalForm()));
  MediaView mediaView = new MediaView(mediaPlayer_video);

  private MediaPlayer mediaPlayer = new MediaPlayer(sound);

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
    startController = loader.getController();
    startController.setMain(this);


    // A Little Music
    mediaPlayer.play();

    primaryStage.setTitle("Rummikub");
    //Scene scene = resolution(root);
    Scene scene = new Scene(root, 1024, 768);
    //scene.getStylesheets().add("view/gameStyle.css"); //TODO: Hide
    primaryStage.setScene(scene);
    //primaryStage.setFullScreen(true);
    primaryStage.show();
    mediaPlayer_video.play();
    startController.setMain(this);

    primaryStage.setOnCloseRequest(e -> {
      System.out.println("klicked  on x");
      //startController.killThreads();
      Platform.exit();
    });
    //--------------------------------------------------------
  }

  //TODO: Catch exception
  @Override
  public void start(Stage primaryStage) throws Exception {
    StackPane root = new StackPane();
    root.getChildren().add(mediaView);
    Scene scene = new Scene(root, 600,350);
    primaryStage.setScene(scene);
    primaryStage.show();
    mediaPlayer_video.play();
    mediaPlayer_video.setOnEndOfMedia(() -> {
      mediaPlayer.stop();
      try {
        hostJoinStage(new Stage());
        primaryStage.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      //Platform.runLater(() -> System.out.println(mediaPlayer.getStatus()));
    });

    /*//FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
    FXMLLoader loader = new FXMLLoader(getClass().getResource("start.fxml"));
    Parent root = loader.load();
    //gameController = loader.getController();
    startController = loader.getController();
    startController.setMain(this);


    // A Little Music
    //mediaPlayer.play();
    root.getChildrenUnmodifiable().add(mediaView);
    mediaPlayer_video.play();

    primaryStage.setTitle("Rummikub");
    //Scene scene = resolution(root);
    Scene scene = new Scene(root, 1024, 768);
    //scene.getStylesheets().add("view/gameStyle.css"); //TODO: Hide
    primaryStage.setScene(scene);
    //primaryStage.setFullScreen(true);
    primaryStage.show();
    mediaPlayer_video.play();
    startController.setMain(this);

    primaryStage.setOnCloseRequest(e -> {
      System.out.println("klicked  on x");
      startController.killThreads();
      Platform.exit();
    });*/
  }

  void stopMusic() {
    this.mediaPlayer.stop();
  }

  /**
   * Creates scene depending on the user device's resolution
   *
   * @param root parent root
   * @return new scene with user's resolution
   */
  Scene resolution(Parent root) {
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    return new Scene(root, screen.width, screen.height);
  }
}

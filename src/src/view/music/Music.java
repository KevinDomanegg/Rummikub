package view.music;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;

public class Music {
  // GAME VIEW MEDIA PLAYERS
  private static MediaPlayer play_pickupStone;
  private static MediaPlayer play_dropStone;
  private static MediaPlayer play_drawStone;

  // PLAYING NOW: FOR START AND WAIT VIEW
  private static MediaPlayer playing_now;


  public static void selectMusic(String view) {
    stopAllMusic();
    try {
      switch (view) {
        case "start":
          Media startView_music = new Media(Music.class.getResource("startMusic.mp3").toURI().toString());
          playing_now = new MediaPlayer(startView_music);
          return;
        case "wait":
          Media waitView_music = new Media(Music.class.getResource("waitingMusic.mp3").toURI().toString());
          playing_now = new MediaPlayer(waitView_music);
          return;
        case "game":
          Media sound_pickupStone = new Media(Music.class.getResource("pickupStone.mp3").toURI().toString());
          Media sound_dropStone = new Media(Music.class.getResource("dropStone.mp3").toURI().toString());
          Media sound_drawStone = new Media(Music.class.getResource("draw.mp3").toURI().toString());
          play_pickupStone = new MediaPlayer(sound_pickupStone);
          play_dropStone = new MediaPlayer(sound_dropStone);
          play_drawStone = new MediaPlayer(sound_drawStone);
      }
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  private static void stopAllMusic() {
    if (playing_now != null) {
      playing_now.stop();
    }
    if (play_pickupStone != null) {
      play_pickupStone.stop();
    }
    if (play_dropStone != null) {
      play_dropStone.stop();
    }
    if (play_drawStone != null) {
      play_drawStone.stop();
    }
  }

  // FOR MUSIC IN START OR WAIT VIEW
  public static void playMusicNow() {
    playing_now.play();
  }

  public static void playSoundOf(String sound) {
    switch (sound) {
      case "pick up stone":
        play_pickupStone.stop();
        play_pickupStone.play();
        return;
      case "drop stone":
        play_dropStone.stop();
        play_dropStone.play();
        return;
      case "draw stone":
        play_dropStone.stop();
        play_drawStone.play();
    }
  }

  public static void muteSoundOfWait() {
    playing_now.pause();
  }


}

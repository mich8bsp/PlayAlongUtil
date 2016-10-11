package com.github.mich8bsp;/**
 * Created by mich8 on 07-Oct-16.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class TabMediaPlayer extends Application implements SongEvents{

    private static final String MUSIC_DIR = "C:\\Music";
    private SongManager songManager;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Play Along Util");

        final List<String> params = getParameters().getRaw();
        final File dir = (params.size() > 0)
                ? new File(params.get(0))
                : new File(MUSIC_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Cannot find audio source directory: " + dir + " please supply a directory as a command line argument");
            Platform.exit();
            return;
        }
        songManager = new SongManager();
        songManager.init(dir,this);

        Group root = new Group();
        scene = new Scene(root);
        onSongChange();
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void onSongChange() {
        SongBundle nextSong = songManager.getNextSong();
       changeSong(nextSong);
    }

    public void changeSong(SongBundle song){
        MediaControl nextSongControl = song.getMediaControl();
        nextSongControl.addEventObserver(this);
        scene.setRoot(nextSongControl);
        song.getMediaPlayer().currentTimeProperty().addListener(ov -> nextSongControl.updateValues());

        song.getMediaPlayer().play();
    }
}

package com.github.mich8bsp;/**
 * Created by mich8 on 07-Oct-16.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class TabMediaPlayer extends Application implements ViewUpdater {

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
        songManager = new SongManager(this);
        songManager.init(dir);

        Group root = new Group();
        scene = new Scene(root);

        //play the first song
        songManager.onSongChange();

        //stage scene
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void updateView(Parent root) {
        scene.setRoot(root);
    }
}

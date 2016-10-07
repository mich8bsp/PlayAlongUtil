package com.github.mich8bsp;/**
 * Created by mich8 on 07-Oct-16.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class TabMediaPlayer extends Application implements SongEvents{

    private static final String MUSIC_DIR = "C:\\Music";
    private SongManager songManager;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Tab Util Player");

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
        songManager.init(dir);

        Group root = new Group();
        scene = new Scene(root, 540, 241);
        onSongChange();

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();

    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void onSongChange() {
        SongBundle nextSong = songManager.getNextSong();
        MediaControl nextSongControl = nextSong.getMediaControl();
        nextSongControl.addEventObserver(this);
        scene.setRoot(nextSongControl);
        nextSong.getMediaPlayer().play();
    }
}

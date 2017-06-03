package com.github.mich8bsp.main;

import com.github.mich8bsp.db.DBConn;
import com.github.mich8bsp.mediaplayer.SongManager;
import io.vertx.core.json.JsonObject;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by mich8 on 07-Oct-16.
 */
public class TabMediaPlayer extends Application {

    private SongManager songManager;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Tab Music Player");

        songManager = new SongManager(root -> scene.setRoot(root));

        DBConn.getDBClient().find(DBConn.COLLECTION_NAME, new JsonObject(), res -> {
            if (res.succeeded()) {
                songManager.init(res.result());
            } else {
                System.out.println("Failed to read tabs from db " + res.cause());
            }
        });

        while (!songManager.getIsInitialized()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

}

package com.github.mich8bsp.mediaplayer;

import com.github.mich8bsp.Utils;
import io.vertx.core.json.JsonObject;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Created by mich8 on 07-Oct-16.
 */
public class SongBundle {

    private MediaControl mediaControl;
    private MediaPlayer mediaPlayer;
    private String songName;

    public SongBundle(JsonObject tab, SongManager songManager) {
        this.songName = Utils.getFullTitle(tab);
        String songURL = Utils.getSongUrl(tab.getString("songPath"));
        Media songMedia = new Media(songURL);
        mediaPlayer = new MediaPlayer(songMedia);
        mediaPlayer.setAutoPlay(false);
        mediaControl = new TabMediaControl(mediaPlayer, new SongStructure(tab.getJsonArray("sections")),
                null, songManager, songManager.getSongsList());
    }


    public MediaControl getMediaControl() {
        return mediaControl;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public String getSongName() {
        return songName;
    }
}

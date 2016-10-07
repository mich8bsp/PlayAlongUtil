package com.github.mich8bsp;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by mich8 on 07-Oct-16.
 */
public class SongBundle {

    private MediaControl mediaControl;
    private Media songMedia;
    private MediaPlayer mediaPlayer;
    private SongStructure songStructure;
    private Path audioFile;
    private Path tabFile;

    public SongBundle(Path tabFile) {
        this.tabFile = tabFile;
        try {
            this.songStructure = new SongStructure(tabFile);
            this.audioFile = getAudioFileByTab(tabFile);
            String songURL ="file:///" + audioFile.toString().replace("\\", "/").replaceAll(" ", "%20");
            songMedia = new Media(songURL);
            mediaPlayer = new MediaPlayer(songMedia);
            mediaPlayer.setAutoPlay(false);
            mediaControl = new MediaControl(mediaPlayer, songStructure);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path getAudioFileByTab(Path tabFile) {
        String songName = tabFile.getFileName().toString().replaceAll(".tabs", "");
        return tabFile.resolveSibling(songName + ".mp3");
    }

    public MediaControl getMediaControl() {
        return mediaControl;
    }

    public Media getMedia() {
        return songMedia;
    }

    public SongStructure getSongStructure() {
        return songStructure;
    }

    public Path getTabFile() {
        return tabFile;
    }

    public Path getAudioFile() {
        return audioFile;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}

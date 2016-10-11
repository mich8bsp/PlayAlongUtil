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
    private SongStructure tabsStructure;
    private SongStructure lyricsStructure;
    private Path audioFile;
    private Path tabFile;
    private Path lyricsFile;
    private String songName;



    public SongBundle(Path tabFile, SongManager songManager) {
        try {
            this.tabFile = tabFile;
            this.songName = resolveSongName(tabFile);
            this.tabsStructure = new SongStructure(tabFile);
            this.lyricsFile = null;
            this.lyricsStructure = null;
            try{
                lyricsFile = resolveLyricsFile();
                lyricsStructure = new SongStructure(lyricsFile);
            }catch (Exception e){
                System.out.println("Couldn't find lyrics file for " + tabFile.getFileName().toString());
            }

            this.audioFile = resolveAudioFile();
            String songURL ="file:///" + audioFile.toString().replace("\\", "/").replaceAll(" ", "%20");
            songMedia = new Media(songURL);
            mediaPlayer = new MediaPlayer(songMedia);
            mediaPlayer.setAutoPlay(false);
            mediaControl = new MediaControl(mediaPlayer, tabsStructure, lyricsStructure, songManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path resolveLyricsFile() {
        return tabFile.resolveSibling(songName + ".lyrics");
    }

    protected static String resolveSongName(Path tabFile){
        return tabFile.getFileName().toString().replaceAll(".tabs", "");
    }

    private Path resolveAudioFile() {
        //currently supports only mp3s, add other extensions here
        return tabFile.resolveSibling(songName + ".mp3");
    }

    public MediaControl getMediaControl() {
        return mediaControl;
    }

    public Media getMedia() {
        return songMedia;
    }

    public SongStructure getTabsStructure() {
        return tabsStructure;
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

    public SongStructure getLyricsStructure() {
        return lyricsStructure;
    }

    public String getSongName() {
        return songName;
    }
}

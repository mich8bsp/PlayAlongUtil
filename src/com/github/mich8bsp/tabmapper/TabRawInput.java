package com.github.mich8bsp.tabmapper;

import java.io.File;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabRawInput {

    private final File audioFile;
    private String title;
    private String artist;
    private String tab;

    public TabRawInput(String title, String artist, String tab, File audioFile) {
        this.title = title;
        this.artist = artist;
        this.tab = tab;
        this.audioFile = audioFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public File getAudioFile() {
        return audioFile;
    }
}

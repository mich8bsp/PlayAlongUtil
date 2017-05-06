package com.github.mich8bsp.tabmapper;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabRawInput {
    private String title;
    private String artist;
    private String tab;

    public TabRawInput(String title, String artist, String tab) {
        this.title = title;
        this.artist = artist;
        this.tab = tab;
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
}

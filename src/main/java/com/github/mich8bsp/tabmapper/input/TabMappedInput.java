package com.github.mich8bsp.tabmapper.input;

import com.github.mich8bsp.tabmapper.songstructure.SongSection;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabMappedInput {

    private File audioFile;
    private List<String> songParts;
    private Map<String, SongSection> partNameToPart;
    private String title;
    private String artist;

    public TabMappedInput setSectionNames(List<String> songParts) {
        this.songParts = songParts;
        return this;
    }

    public TabMappedInput setSectionMapping(Map<String, SongSection> mapping) {
        this.partNameToPart = mapping;
        return this;
    }

    public TabMappedInput setAudioFile(File audioFile) {
        this.audioFile = audioFile;
        return this;
    }

    public TabMappedInput setTitle(String title) {
        this.title = title;
        return this;
    }

    public TabMappedInput setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public File getAudioFile() {
        return audioFile;
    }

    public List<String> getSongParts() {
        return songParts;
    }

    public Map<String, SongSection> getPartNameToPart() {
        return partNameToPart;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
}

package com.github.mich8bsp.tabmapper.input;

import com.github.mich8bsp.tabmapper.songstructure.SongSection;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabMappedInput {

    private final File audioFile;
    private List<String> songParts;
    private Map<String, SongSection> partNameToPart;

    public TabMappedInput(List<String> songParts, Map<String, SongSection> partNameToPart, File audioFile) {
        this.songParts = songParts;
        this.partNameToPart = partNameToPart;
        this.audioFile = audioFile;
    }

    public List<String> getSongParts() {
        return songParts;
    }

    public void setSongParts(List<String> songParts) {
        this.songParts = songParts;
    }

    public Map<String, SongSection> getPartNameToPart() {
        return partNameToPart;
    }

    public void setPartNameToPart(Map<String, SongSection> partNameToPart) {
        this.partNameToPart = partNameToPart;
    }

    public File getAudioFile() {
        return audioFile;
    }
}

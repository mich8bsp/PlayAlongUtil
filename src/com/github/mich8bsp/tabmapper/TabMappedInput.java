package com.github.mich8bsp.tabmapper;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabMappedInput {


    private final File audioFile;
    private List<String> songParts;
    private Map<String, List<String>> partNameToPart;

    public TabMappedInput(List<String> songParts, Map<String, List<String>> partNameToPart, File audioFile) {
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

    public Map<String, List<String>> getPartNameToPart() {
        return partNameToPart;
    }

    public void setPartNameToPart(Map<String, List<String>> partNameToPart) {
        this.partNameToPart = partNameToPart;
    }

    public File getAudioFile() {
        return audioFile;
    }
}

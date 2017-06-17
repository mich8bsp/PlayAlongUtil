package com.github.mich8bsp.tabmapper.songstructure;

import com.github.mich8bsp.tabmapper.parsing.ChordSequence;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mich8 on 12-May-17.
 * <p>
 * Tab Segment represents a single line of music ( a single line of lyrics, its chords and its tabs)
 * Although musically it represents a single line, the segment itself consists of several lines of text
 */
public class TabSegment {
    private String lyrics;
    private List<String> tabs;
    private ChordSequence chords;
    private List<String> rawText;

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public List<String> getTabs() {
        return tabs;
    }

    public void setTabs(List<String> tabs) {
        this.tabs = tabs;
    }

    public ChordSequence getChords() {
        return chords;
    }

    public void setChords(ChordSequence chords) {
        this.chords = chords;
    }

    public void addRawTextLine(String line){
        if(rawText==null){
            rawText = new LinkedList<>();
        }
        rawText.add(line);
    }

    public List<String> getLines() {
        //if we opted not to parse the tabs semantically - return the raw text
        if(rawText!=null){
            return rawText;
        }
        List<String> lines = new LinkedList<>();
        if (chords != null) {
            String chordsLine = chords.getElements()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" "));
            lines.add(chordsLine);
        }
        if (tabs != null) {
            lines.addAll(tabs);
        }
        if (lyrics != null) {
            lines.add(lyrics);
        }
        return lines;
    }

    public boolean isEmpty() {
        return chords == null && tabs == null && lyrics == null && rawText == null;
    }
}

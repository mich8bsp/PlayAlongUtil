package com.github.mich8bsp.tabmapper.songstructure;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mich8 on 12-May-17.
 *
 * Song section represents parts of the songs such as Intro, Verse, Chorus, Solo etc...
 */
public class SongSection {

    private List<TabSegment> segments = new LinkedList<>();


    private SongSection referencedSection;

    public void addSegment(TabSegment segment) {
        segments.add(segment);
    }

    public List<String> getLines() {
        return segments.stream()
                .map(TabSegment::getLines)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return segments.isEmpty() || segments.stream()
                .map(TabSegment::isEmpty)
                .reduce(true, (x,y)->x&&y);
    }

    public void setReferencedSection(SongSection referencedSection) {
        this.referencedSection = referencedSection;
    }


    public SongSection getReferencedSection() {
        return referencedSection;
    }

    public List<TabSegment> getSegments() {
        return segments;
    }

}

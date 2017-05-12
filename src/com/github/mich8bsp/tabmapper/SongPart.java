package com.github.mich8bsp.tabmapper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mich8 on 12-May-17.
 */
public class SongPart {
    private List<TabSegment> segments = new LinkedList<>();

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
                .map(x->x.getChords()==null && x.getTabs()==null && x.getLyrics()==null)
                .findAny()
                .orElse(true);
    }
}

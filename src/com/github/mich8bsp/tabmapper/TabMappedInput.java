package com.github.mich8bsp.tabmapper;

import java.util.List;
import java.util.Map;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabMappedInput {
    private List<String> songParts;
    private Map<String, List<String>> partNameToPart;

    public TabMappedInput(List<String> songParts, Map<String, List<String>> partNameToPart) {
        this.songParts = songParts;
        this.partNameToPart = partNameToPart;
    }
}

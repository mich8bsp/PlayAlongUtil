package com.github.mich8bsp;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mich8 on 02-Oct-16.
 */
public class SongPart {
    private List<String> tabs = new LinkedList<>();

    public void addLine(String line) {
        tabs.add(line);
    }

    public String getAsString(){
        return String.join("\n", tabs);
    }
}

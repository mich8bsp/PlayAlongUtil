package com.github.mich8bsp;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mich8 on 02-Oct-16.
 */
public class SongPart {
    private List<String> songPartInLines = new LinkedList<>();

    public void addLine(String line) {
        songPartInLines.add(line);
    }

    public String getAsString(){
        return String.join("\n", songPartInLines);
    }
}

package com.github.mich8bsp.tabmapper.songstructure;

/**
 * Created by Michael Bespalov on 02-Jun-17.
 */
public class Note {
    public ENoteRoot root;
    public ENoteIntonation intonation = ENoteIntonation.NONE;

    @Override
    public String toString() {
        return root.name() + intonation;
    }
}

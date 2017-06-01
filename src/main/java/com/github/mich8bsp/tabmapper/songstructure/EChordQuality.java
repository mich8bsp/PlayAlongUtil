package com.github.mich8bsp.tabmapper.songstructure;

/**
 * Created by mich8 on 12-May-17.
 */
public enum EChordQuality {
    //MAJOR_IMPL is the implied major, for example the chord G7 is actually GM7
    MAJOR_IMPL(""), MAJOR_EXPL("maj"), MAJOR("M"), MINOR("m"), AUGMENTED("aug"), DIMINISHED("dim");

    private final String stringRepr;

    EChordQuality(String s) {
        stringRepr = s;
    }

    @Override
    public String toString() {
        return stringRepr;
    }
}

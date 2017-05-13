package com.github.mich8bsp.tabmapper.songstructure;

/**
 * Created by mich8 on 12-May-17.
 */
public class Chord implements IMusicElement {

    private EChordRoot root;
    private EChordIntonation intonation = EChordIntonation.NONE;
    private EChordQuality quality = EChordQuality.MAJOR_IMPL;
    int interval = 0;

    public Chord(EChordRoot root){
        this.root = root;
    }

    public Chord setIntonation(EChordIntonation intonation){
        this.intonation = intonation;
        return this;
    }

    public Chord setQuality(EChordQuality quality){
        this.quality =quality;
        return this;
    }

    public Chord setInterval(int interval){
        this.interval = interval;
        return this;
    }

    @Override
    public String toString() {
        return root.name() + intonation + quality + ((interval>0) ? interval : "");
    }
}

package com.github.mich8bsp.tabmapper.songstructure;

/**
 * Created by mich8 on 12-May-17.
 */
public class Chord implements IMusicElement {

    private Note chordRoot;
    private EChordQuality quality = EChordQuality.MAJOR_IMPL;
    private int interval = 0;
    private Note altBass;

    public Chord(Note chordRoot){
        this.chordRoot = chordRoot;
    }

    public Chord setQuality(EChordQuality quality){
        this.quality =quality;
        return this;
    }

    public Chord setInterval(int interval){
        this.interval = interval;
        return this;
    }

    public Chord setAltBass(Note altBass){
        this.altBass = altBass;
        return this;
    }
    public Chord setChordRoot(Note chordRoot) {
        this.chordRoot = chordRoot;
        return this;
    }
    @Override
    public String toString() {
        return chordRoot.toString()
                + quality + ((interval>0) ? interval : "")
                + (altBass!=null ? "/" + altBass : "");
    }


}

package com.github.mich8bsp.tabmapper;

/**
 * Created by mich8 on 12-May-17.
 */
public class Chord implements IMusicElement{
    enum EChordRoot {
        A, B, C, D, E, F, G
    }

    enum EChordQuality {
        MAJOR_IMPL(""), MAJOR("M"), MINOR("m"), AUGMENTED("aug"), DIMINISHED("dim");

        private final String stringRepr;

        EChordQuality(String s) {
            stringRepr = s;
        }

        @Override
        public String toString(){
            return stringRepr;
        }
    }

    enum EChordIntonation {
        NONE(""), FLAT("b"), SHARP("#");

        private String stringRepr = "";
        EChordIntonation(String s) {
            stringRepr = s;
        }

        @Override
        public String toString() {
            return stringRepr;
        }
    }


    private EChordRoot root;
    private EChordIntonation intonation = EChordIntonation.NONE;
    private EChordQuality quality = EChordQuality.MAJOR_IMPL;
    int interval = 0;

    public Chord(String chordText){
        int currentCharIndex = 0;
        root = EChordRoot.valueOf(chordText.substring(currentCharIndex, currentCharIndex+1));
        currentCharIndex++;
        if(currentCharIndex>=chordText.length()){
            return;
        }
        String nextChar = chordText.substring(currentCharIndex, currentCharIndex+1);
        for(EChordIntonation intonation : EChordIntonation.values()){
            if(nextChar.equals(intonation.toString())){
                this.intonation = intonation;
                currentCharIndex++;
                if(currentCharIndex>=chordText.length()){
                    return;
                }
                break;
            }
        }
        nextChar = chordText.substring(currentCharIndex, currentCharIndex+1);
        for(EChordQuality quality : EChordQuality.values()){
            if(nextChar.equals(quality.toString())){
                this.quality = quality;
                currentCharIndex++;
                if(currentCharIndex>=chordText.length()){
                    return;
                }
                break;
            }
        }
        try{
            interval = Integer.parseInt(nextChar);
        }catch (Exception e){

        }
    }

    @Override
    public String toString() {
        return root.name() + intonation + quality + ((interval>0) ? interval : "");
    }
}

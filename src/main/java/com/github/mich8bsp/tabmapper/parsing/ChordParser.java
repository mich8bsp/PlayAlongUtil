package com.github.mich8bsp.tabmapper.parsing;

import com.github.mich8bsp.tabmapper.songstructure.*;

/**
 * Created by mich8 on 12-May-17.
 */
public class ChordParser {

    public static Note parseChordRoot(String chordText) {
        int currentCharIndex = 0;
        ENoteRoot root = ENoteRoot.valueOf(chordText.substring(currentCharIndex, currentCharIndex + 1));
        Note chordRoot = new Note();
        chordRoot.root = root;
        currentCharIndex += root.name().length();
        if (currentCharIndex >= chordText.length()) {
            return chordRoot;
        }
        String nextChar = chordText.substring(currentCharIndex, currentCharIndex + 1);
        for (ENoteIntonation intonation : ENoteIntonation.values()) {
            if (nextChar.equals(intonation.toString())) {
                chordRoot.intonation = intonation;
                break;
            }
        }
        return chordRoot;
    }

    public static Chord parseChord(String chordText) {
        String[] splitByBass = chordText.split("/");
        if (splitByBass.length > 1) {
            Chord mainChord = parseChord(splitByBass[0]);
            Note altBass = parseChordRoot(splitByBass[1]);
            if (mainChord != null && altBass != null) {
                mainChord.setAltBass(altBass);
            }
            return mainChord;
        }
        Note chordRoot = parseChordRoot(chordText);
        if (chordRoot == null) {
            return null;
        }
        Chord parsedChord = new Chord(chordRoot);
        int currentCharIndex = chordRoot.toString().length();

        if (currentCharIndex >= chordText.length()) {
            return parsedChord;
        }
        String restOfChord = chordText.substring(currentCharIndex, chordText.length());
        for (EChordQuality quality : EChordQuality.values()) {
            if (!quality.toString().isEmpty() && restOfChord.startsWith(quality.toString())) {
                parsedChord.setQuality(quality);
                currentCharIndex += quality.toString().length();
                if (currentCharIndex >= chordText.length()) {
                    return parsedChord;
                }
                break;
            }
        }
        try {
            String nextChar = chordText.substring(currentCharIndex, currentCharIndex + 1);
            int interval = Integer.parseInt(nextChar);
            parsedChord.setInterval(interval);
        } catch (Exception e) {

        }
        return parsedChord;
    }
}

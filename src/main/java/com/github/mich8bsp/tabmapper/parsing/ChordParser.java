package com.github.mich8bsp.tabmapper.parsing;

import com.github.mich8bsp.tabmapper.songstructure.Chord;
import com.github.mich8bsp.tabmapper.songstructure.EChordIntonation;
import com.github.mich8bsp.tabmapper.songstructure.EChordQuality;
import com.github.mich8bsp.tabmapper.songstructure.EChordRoot;

/**
 * Created by mich8 on 12-May-17.
 */
public class ChordParser {

    public static Chord parseChord(String chordText){
        int currentCharIndex = 0;
        EChordRoot root = EChordRoot.valueOf(chordText.substring(currentCharIndex, currentCharIndex+1));
        Chord parsedChord = new Chord(root);
        currentCharIndex++;
        if(currentCharIndex>=chordText.length()){
            return parsedChord;
        }
        String nextChar = chordText.substring(currentCharIndex, currentCharIndex+1);
        for(EChordIntonation intonation : EChordIntonation.values()){
            if(nextChar.equals(intonation.toString())){
                parsedChord.setIntonation(intonation);
                currentCharIndex++;
                if(currentCharIndex>=chordText.length()){
                    return parsedChord;
                }
                break;
            }
        }
        String restOfChord = chordText.substring(currentCharIndex, chordText.length());
        for(EChordQuality quality : EChordQuality.values()){
            if(!quality.toString().isEmpty() && restOfChord.startsWith(quality.toString())){
                parsedChord.setQuality(quality);
                currentCharIndex += quality.toString().length();
                if(currentCharIndex>=chordText.length()){
                    return parsedChord;
                }
                break;
            }
        }
        try{
            nextChar = chordText.substring(currentCharIndex, currentCharIndex+1);
            int interval = Integer.parseInt(nextChar);
            parsedChord.setInterval(interval);
        }catch (Exception e){

        }
        return parsedChord;
    }
}

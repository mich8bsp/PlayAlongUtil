package com.github.mich8bsp.tabmapper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mich8 on 12-May-17.
 */
public class ChordSequence {
    List<IMusicElement> elementsSequence = new LinkedList<>();

    public ChordSequence(List<IMusicElement> elements) {
        this.elementsSequence = elements;
    }

    public List<IMusicElement> getElements() {
        return elementsSequence;
    }

    private static class Rest implements IMusicElement{

        @Override
        public String toString() {
            return "";//String.join(" ", Collections.nCopies(numOfSpaces, " "));
        }
    }

    public static ChordSequence buildChordSequence(String text){
        List<IMusicElement> elements = new LinkedList<>();
        for(String element : text.split(" ")){
            if(element.isEmpty()){
                elements.add(new Rest());
            }else{
                try{
                    Chord chord = new Chord(element);
                    elements.add(chord);
                }catch (Exception e){
                    return null;
                }
            }
        }
        return new ChordSequence(elements);
    }

}

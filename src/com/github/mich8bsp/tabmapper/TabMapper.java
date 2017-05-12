package com.github.mich8bsp.tabmapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabMapper {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("res"+ File.separator+"test-tab"));
        String tab = lines.stream().collect(Collectors.joining("\n"));
        TabRawInput rawInput = new TabRawInput("songysong", "artyart", tab, null);
        parseTab(rawInput);
    }



    public static TabMappedInput parseTab(TabRawInput tab){
        List<String> tabLines = Arrays.asList(tab.getTab().split("\n"));

        List<String> songParts = new LinkedList<>();
        Map<String, SongPart> partNameToPart = new HashMap<>();

        SongPart currentPart = new SongPart();
        TabSegment currentTabSegment = new TabSegment();
        currentPart.addSegment(currentTabSegment);
        songParts.add(tab.getTitle());
        partNameToPart.put(tab.getTitle(), currentPart);

        for(String line : tabLines){
            if(line.isEmpty()){
                continue;
            }
            if(line.contains("Capo")){
                //TODO: handle capo
                continue;
            }
            if(isSongPartName(line)){
                String currentPartName = line.substring(line.indexOf('[')+1, line.lastIndexOf(']'));

                songParts.add(currentPartName);

                currentPart = new SongPart();
                currentTabSegment = new TabSegment();
                currentPart.addSegment(currentTabSegment);

                partNameToPart.put(currentPartName, currentPart);
                continue;
            }
            ChordSequence chordSequence = ChordSequence.buildChordSequence(line);

            if (chordSequence != null) {
                if(currentTabSegment.getChords()!=null){
                    currentTabSegment = new TabSegment();
                    currentPart.addSegment(currentTabSegment);
                }
                currentTabSegment.setChords(chordSequence);
                continue;
            }

            if(isTabLine(line)){
                currentTabSegment.getTabs().add(line);
                continue;
            }
            currentTabSegment.setLyrics(line);
        }

        List<String> partsToRemove = new LinkedList<>();
        for(String part: songParts){
            if(partNameToPart.get(part).isEmpty()){
                partNameToPart.remove(part);
                partsToRemove.add(part);
            }
        }

        songParts.removeAll(partsToRemove);


        return new TabMappedInput(songParts, partNameToPart, tab.getAudioFile());
    }

    private static boolean isSongPartName(String line){
        return line.contains("[") && line.contains("]");
    }

    private static boolean isTabLine(String line){
        return line.contains("--");
    }



}

package com.github.mich8bsp.tabmapper.parsing;

import com.github.mich8bsp.tabmapper.songstructure.SongSection;
import com.github.mich8bsp.tabmapper.input.TabRawInput;
import com.github.mich8bsp.tabmapper.input.TabMappedInput;
import com.github.mich8bsp.tabmapper.songstructure.TabSegment;

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

    //Test
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("res"+ File.separator+"test-tab"));
        String tab = lines.stream().collect(Collectors.joining("\n"));
        TabRawInput rawInput = new TabRawInput("songysong", "artyart", tab, null);
        parseTab(rawInput);
    }



    public static TabMappedInput parseTab(TabRawInput tab){
        List<String> tabLines = Arrays.asList(tab.getTab().split("\n"));

        //this holds the order of the song parts
        List<String> songSectionNames = new LinkedList<>();
        Map<String, SongSection> sectionNameToSection = new HashMap<>();

        //if for some reason the whole tab came as a single section, we add it with name of song and later we'll split it dynamically
        SongSection currentPart = new SongSection();
        TabSegment currentTabSegment = new TabSegment();
        currentPart.addSegment(currentTabSegment);
        songSectionNames.add(tab.getTitle());
        sectionNameToSection.put(tab.getTitle(), currentPart);

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

                songSectionNames.add(currentPartName);

                currentPart = new SongSection();
                currentTabSegment = new TabSegment();
                currentPart.addSegment(currentTabSegment);

                sectionNameToSection.put(currentPartName, currentPart);
                continue;
            }
            ChordSequence chordSequence = ChordSequence.buildChordSequence(line);

            if (chordSequence != null) {
                if(currentTabSegment.getChords()!=null){
                    //there can be no two chord sequences in a single segment, so we split
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
        for(String part: songSectionNames){
            if(sectionNameToSection.get(part).isEmpty()){
                sectionNameToSection.remove(part);
                partsToRemove.add(part);
            }
        }

        songSectionNames.removeAll(partsToRemove);


        return new TabMappedInput(songSectionNames, sectionNameToSection, tab.getAudioFile());
    }

    private static boolean isSongPartName(String line){
        return line.contains("[") && line.contains("]");
    }

    private static boolean isTabLine(String line){
        return line.contains("--");
    }



}

package com.github.mich8bsp.tabmapper.parsing;

import com.github.mich8bsp.tabmapper.input.TabMappedInput;
import com.github.mich8bsp.tabmapper.input.TabRawInput;
import com.github.mich8bsp.tabmapper.songstructure.SongSection;
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

    private static int globalCounter = 0;

    //Test
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("res" + File.separator + "test-tab"));
        String tab = lines.stream().collect(Collectors.joining("\n"));
        TabRawInput rawInput = new TabRawInput("songysong", "artyart", tab, null);
        parseTab(rawInput);
    }


    public static TabMappedInput parseTab(TabRawInput tab) {
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

        for (String line : tabLines) {
            if (line.isEmpty()) {
                continue;
            }
            if (line.contains("Capo")) {
                //TODO: handle capo
                continue;
            }
            if (isSongPartName(line)) {
                String currentPartName = line.substring(line.indexOf('[') + 1, line.lastIndexOf(']'));

                SongSection referencedSection = sectionNameToSection.get(currentPartName);
                if (referencedSection != null) {
                    currentPartName += ++globalCounter;
                }

                songSectionNames.add(currentPartName);

                currentPart = new SongSection();
                currentTabSegment = new TabSegment();
                currentPart.addSegment(currentTabSegment);
                if (referencedSection != null) {
                    currentPart.setReferencedSection(referencedSection);
                }

                sectionNameToSection.put(currentPartName, currentPart);
                continue;
            }
            ChordSequence chordSequence = null;
            if (!isTabLine(line)) {
                chordSequence = ChordSequence.buildChordSequence(line);
            }
            if (chordSequence != null) {
                if (currentTabSegment.getChords() != null) {
                    //there can be no two chord sequences in a single segment, so we split
                    currentTabSegment = new TabSegment();
                    currentPart.addSegment(currentTabSegment);
                }
                currentTabSegment.setChords(chordSequence);
                continue;
            }

            if (isTabLine(line)) {
                if (currentTabSegment.getTabs() == null) {
                    currentTabSegment.setTabs(new LinkedList<>());
                }
                currentTabSegment.getTabs().add(line);
                continue;
            }

            if (currentTabSegment.getLyrics() != null) {
                currentTabSegment = new TabSegment();
                currentPart.addSegment(currentTabSegment);
            }
            currentTabSegment.setLyrics(line);
        }
        fillInMissingParts(sectionNameToSection.values());
        removeEmpty(songSectionNames, sectionNameToSection);

        return new TabMappedInput()
                .setArtist(tab.getArtist())
                .setTitle(tab.getTitle())
                .setAudioFile(tab.getAudioFile())
                .setSectionNames(songSectionNames)
                .setSectionMapping(sectionNameToSection);
    }

    private static void fillInMissingParts(Collection<SongSection> sections) {
        for (SongSection section : sections) {
            if (section.getReferencedSection() != null) {
                SongSection referencedSection = section.getReferencedSection();
                for (int i = 0; i < Math.min(section.getSegments().size(), referencedSection.getSegments().size()); i++) {
                    TabSegment currentSegment = section.getSegments().get(i);
                    TabSegment referencedSegment = referencedSection.getSegments().get(i);
                    if (currentSegment.getTabs() == null && referencedSegment.getTabs() != null) {
                        currentSegment.setTabs(referencedSegment.getTabs());
                    }
                    if (currentSegment.getChords() == null && referencedSegment.getChords() != null) {
                        currentSegment.setChords(referencedSegment.getChords());
                    }
                }
            }
        }
    }

    private static void removeEmpty(List<String> songSectionNames, Map<String, SongSection> sectionNameToSection) {

        List<String> partsToRemove = new LinkedList<>();
        for (String part : songSectionNames) {
            if (sectionNameToSection.get(part).isEmpty()) {
                sectionNameToSection.remove(part);
                partsToRemove.add(part);
            }
        }

        songSectionNames.removeAll(partsToRemove);
    }

    private static boolean isSongPartName(String line) {
        return line.contains("[") && line.contains("]");
    }

    private static boolean isTabLine(String line) {
        return line.contains("--");
    }


}

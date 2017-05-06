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
        List<String> lines = Files.readAllLines(Paths.get("test-tab"));
        String tab = lines.stream().collect(Collectors.joining("\n"));
        TabRawInput rawInput = new TabRawInput("songysong", "artyart", tab, null);
        parseTab(rawInput);
    }

    public static TabMappedInput parseTab(TabRawInput tab){
        List<String> tabLines = Arrays.asList(tab.getTab().split("\n"));

        List<String> songParts = new LinkedList<>();
        Map<String, List<String>> partNameToPart = new HashMap<>();

        List<String> currentPart = new ArrayList<>();
        for(String line : tabLines){
            if(line.isEmpty()){
                continue;
            }
            if(line.contains("Capo")){
                //TODO: handle capo
                continue;
            }
            if(line.contains("[") && line.contains("]")){
                String currentPartName = line.substring(line.indexOf('[')+1, line.lastIndexOf(']'));
                songParts.add(currentPartName);
                currentPart = new ArrayList<>();
                partNameToPart.put(currentPartName, currentPart);
                continue;
            }
            currentPart.add(line);
        }

        return new TabMappedInput(songParts, partNameToPart, tab.getAudioFile());
    }
}

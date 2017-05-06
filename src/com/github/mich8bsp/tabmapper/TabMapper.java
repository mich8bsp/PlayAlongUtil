package com.github.mich8bsp.tabmapper;

import com.github.mich8bsp.SongBundle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabMapper {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("test-tab"));
        String tab = lines.stream().collect(Collectors.joining("\n"));
        TabRawInput rawInput = new TabRawInput("songysong", "artyart", tab);
        parseTab(rawInput);
    }

    public static void parseTab(TabRawInput tab){
        System.out.println("Parsing " + tab.getTab());
        //TODO: implement parsing
    }
}

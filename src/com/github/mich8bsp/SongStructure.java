package com.github.mich8bsp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by mich8 on 02-Oct-16.
 */
public class SongStructure {
    //time in sec to name of song part
    private Map<Double, String> songStructure = new HashMap<>();
    //song part name to song part [verse, chorus, bridge etc...]
    private Map<String, SongPart> songParts = new HashMap<>();

    public SongStructure(Path file) throws IOException {
        List<String> allLines = Files.readAllLines(file);
        buildSong(allLines);
    }

    private void buildSong(List<String> tab) {
        SongPart currentPartToFill = null;
        for(String line : tab){
            if(line.contains("-")){
                // marker line would be [songPart]-time, for example: [Verse 2]-04:32
                String songPart = line.substring(0, line.lastIndexOf("-"));
                String time = line.substring(line.lastIndexOf("-")+1, line.length());
                if(songPart.matches("\\[.*\\]")){
                    if(time.matches("[0-9]+:[0-9]+")){
                        songPart = songPart.replaceAll("\\[", "");
                        songPart = songPart.replaceAll("]", "");
                        if(songParts.get(songPart) ==null){
                            currentPartToFill = new SongPart();
                            songParts.put(songPart, currentPartToFill);
                        }else{
                            //we already have the content of this song part, so we don't need to fill it
                            currentPartToFill=null;
                        }
                        int minutes = Integer.parseInt(time.split(":")[0]);
                        int seconds = Integer.parseInt(time.split(":")[1]);
                        double timeInSeconds = minutes*60 + seconds;
                        songStructure.put(timeInSeconds, songPart);
                        continue;
                    }
                }
            }
            if(currentPartToFill!=null){
                //for each time that is not a marker, we add the line to current part that we iterate on
                currentPartToFill.addLine(line);
            }
        }
    }


    public String getCurrentPart(double time) {
        //we want to find the start time of this part, so we need the latest start time that is still before the queried time
        Optional<Double> partTime= songStructure.keySet().stream().filter(x->x<=time).sorted((t1, t2)-> (-1)*Double.compare(t1,t2)).findFirst();
        if(partTime.isPresent()){
            String partName = songStructure.get(partTime.get());
            SongPart songPart = songParts.get(partName);
            return songPart.getAsString();
        }
        return "";
    }
}

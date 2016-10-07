package com.github.mich8bsp;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by mich8 on 07-Oct-16.
 */
public class SongManager {

    private int nextSongIndex = 0;
    private boolean toShuffle = false;
    private List<SongBundle> allSongBundles = new ArrayList<>();

    public void init(File dir) {
        List<Path> tabFiles = new LinkedList<>();
        findTabFiles(dir.toPath(), tabFiles);
        tabFiles.forEach(tab -> allSongBundles.add(new SongBundle(tab)));
    }

    private void findTabFiles(Path dir, List<Path> tabFiles) {
        try(DirectoryStream<Path> stream  = Files.newDirectoryStream(dir)){
            for(Path entry : stream){
                if(Files.isDirectory(entry)){
                    findTabFiles(entry, tabFiles);
                }else {
                    if (entry.getFileName().toString().endsWith("tabs")){
                        tabFiles.add(entry);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setToShuffle(boolean toShuffle){
        this.toShuffle = toShuffle;
    }

    public SongBundle getNextSong() {
        int currentSongIndex = nextSongIndex;
        if(toShuffle){
            while(nextSongIndex==currentSongIndex) {
                nextSongIndex = (int) (Math.random() * allSongBundles.size());
            }
        }else{
            nextSongIndex = (nextSongIndex+1)%allSongBundles.size();
        }

        return allSongBundles.get(currentSongIndex);
    }
}

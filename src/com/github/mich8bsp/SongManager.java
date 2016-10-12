package com.github.mich8bsp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by mich8 on 07-Oct-16.
 */
public class SongManager implements SongEvents, SongControls {

    private final ViewUpdater viewUpdater;
    private int nextSongIndex = 0;
    private int currentSongIndex = 0;
    private boolean toShuffle = false;
    private List<SongBundle> allSongBundles = new ArrayList<>();
    private ObservableList<String> songNamesList;

    public SongManager(ViewUpdater viewUpdater){
        this.viewUpdater = viewUpdater;
    }

    public void init(File dir) {
        List<Path> tabFiles = new LinkedList<>();
        findTabFiles(dir.toPath(), tabFiles);
        songNamesList = getSongNames(tabFiles);
        tabFiles.forEach(tab -> allSongBundles.add(new SongBundle(tab, this)));
    }

    private void findTabFiles(Path dir, List<Path> tabFiles) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    findTabFiles(entry, tabFiles);
                } else {
                    if (entry.getFileName().toString().endsWith("tabs")) {
                        tabFiles.add(entry);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setToShuffle(boolean toShuffle) {
        this.toShuffle = toShuffle;
    }

    public SongBundle getNextSong() {
        currentSongIndex = nextSongIndex;
        findNextSongIndex();
        return allSongBundles.get(currentSongIndex);
    }

    private void findNextSongIndex() {
        if (toShuffle) {
            while (nextSongIndex == currentSongIndex) {
                nextSongIndex = (int) (Math.random() * allSongBundles.size());
            }
        } else {
            nextSongIndex = (currentSongIndex + 1) % allSongBundles.size();
        }
    }

    public ObservableList<String> getSongNames(List<Path> tabFiles) {
        ObservableList<String> songNames = FXCollections.observableArrayList();
        IntStream.range(0, tabFiles.size()).forEach(songIndex -> songNames.add(songIndex, SongBundle.resolveSongName(tabFiles.get(songIndex))));
        return songNames;
    }

    public ListView<String> getSongsList() {
        ListView<String> songListView = new ListView<>();
        songListView.setItems(songNamesList);
        songListView.getSelectionModel().selectedItemProperty().addListener(
                (ov, old_val, new_val) -> {
                    //if user clicked on a song in song list, it's name is in new_val
                    SongBundle oldSong = allSongBundles.get(currentSongIndex);
                    for (int i = 0; i < allSongBundles.size(); i++) {
                        SongBundle newSong = allSongBundles.get(i);
                        //we find the new song in all the bundles and switch songs
                        if (newSong.getSongName().equals(new_val)) {
                            //stop the old song
                            oldSong.getMediaPlayer().stop();
                            //update current and next song indexes
                            currentSongIndex = i;
                            findNextSongIndex();
                            //change to new song
                            changeSong(newSong);
                            return;
                        }
                    }
                }
        );
        return songListView;
    }

    /** Find next song to play (consecutive or random), and change to that song
     *
     */
    @Override
    public void onSongChange() {
        SongBundle nextSong = getNextSong();
        changeSong(nextSong);
    }

    /** change to specific song
     *
     * @param song
     */
    public void changeSong(SongBundle song) {
        MediaControl nextSongControl = song.getMediaControl();
        viewUpdater.updateView(nextSongControl);
        song.getMediaPlayer().currentTimeProperty().addListener(ov -> nextSongControl.updateValues());
        song.getMediaPlayer().play();
    }

    @Override
    public void onShuffleChanged() {
        setToShuffle(!toShuffle);
        findNextSongIndex();
    }


    @Override
    public boolean isShuffleOn() {
        return toShuffle;
    }
}

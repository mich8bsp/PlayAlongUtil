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
public class SongManager {

    private int nextSongIndex = 0;
    private int currentSongIndex = 0;
    private boolean toShuffle = false;
    private List<SongBundle> allSongBundles = new ArrayList<>();
    private ObservableList<String> songsList;
    private TabMediaPlayer player;

    public void init(File dir, TabMediaPlayer player) {
        List<Path> tabFiles = new LinkedList<>();
        findTabFiles(dir.toPath(), tabFiles);
        this.player = player;
        songsList = getSongNames(tabFiles);
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

    public ListView getSongsList() {
        ListView songListView = new ListView();
        songListView.setItems(songsList);
        songListView.getSelectionModel().selectedItemProperty().addListener(
                (ov, old_val, new_val) -> {
                    SongBundle oldSong = allSongBundles.get(currentSongIndex);
                    for (int i = 0; i < allSongBundles.size(); i++) {
                        SongBundle newSong = allSongBundles.get(i);
                        if (newSong.getSongName().equals(new_val)) {
                            oldSong.getMediaPlayer().stop();
                            oldSong.getMediaControl().getSongEventObservers().clear();
                            currentSongIndex = i;
                            findNextSongIndex();
                            player.changeSong(newSong);
                            return;
                        }
                    }
                }
        );
        return songListView;
    }
}

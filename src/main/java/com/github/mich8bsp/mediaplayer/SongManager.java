package com.github.mich8bsp.mediaplayer;

import io.vertx.core.json.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Created by mich8 on 07-Oct-16.
 */
public class SongManager implements ISongEvents {

    private final Consumer<Parent> viewUpdater;
    private int nextSongIndex = 0;
    private int currentSongIndex = 0;
    private boolean toShuffle = false;
    private List<SongBundle> allSongBundles = new ArrayList<>();
    private ObservableList<String> songNamesList;

    private AtomicBoolean isInitialized = new AtomicBoolean(false);

    public SongManager(Consumer<Parent> viewUpdater){
        this.viewUpdater = viewUpdater;
    }

    public void init(List<JsonObject> result) {
        songNamesList = FXCollections.observableArrayList();
        result.stream()
                .map(x->x.getString("artist") + " - " + x.getString("title"))
                .forEach(x->songNamesList.add(x));

        result.forEach(tab -> allSongBundles.add(new SongBundle(tab, this)));
        isInitialized.set(true);
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
                            if(!newSong.getSongName().equals(oldSong.getSongName())) {
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
        viewUpdater.accept(nextSongControl);
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

    public boolean getIsInitialized() {
        return isInitialized.get();
    }
}

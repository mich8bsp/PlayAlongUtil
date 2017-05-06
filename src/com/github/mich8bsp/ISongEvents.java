package com.github.mich8bsp;

/**
 * Created by mich8 on 07-Oct-16.
 */
public interface ISongEvents {

    void onSongChange();

    void changeSong(SongBundle song);

}

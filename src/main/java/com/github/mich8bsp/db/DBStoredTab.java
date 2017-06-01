package com.github.mich8bsp.db;

import com.github.mich8bsp.Utils;
import com.github.mich8bsp.tabmapper.input.TabMappedInput;
import com.github.mich8bsp.tabmapper.view.StatefulText;
import io.vertx.core.json.JsonObject;
import javafx.util.Duration;

import java.util.List;

/**
 * Created by mich8 on 13-May-17.
 */
public class DBStoredTab {

    private String songPath;
    private String title;
    private String artist;
    private List<StatefulText<Duration>> mappedSections;

    public DBStoredTab(TabMappedInput mappedInput, List<StatefulText<Duration>> songParts) {
        this.songPath = mappedInput.getAudioFile().getAbsolutePath();
        this.artist = mappedInput.getArtist();
        this.title = mappedInput.getTitle();
        this.mappedSections = songParts;
    }

    public List<StatefulText<Duration>> getMappedSections() {
        return mappedSections;
    }

    public JsonObject toJson() {
        JsonObject mappedJson = new JsonObject();
        mappedJson.put("title", title);
        mappedJson.put("artist", artist);
        mappedJson.put("songPath", songPath);
        mappedJson.put("sections", Utils.toJson(mappedSections));
        return mappedJson;
    }

}

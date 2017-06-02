package com.github.mich8bsp.mediaplayer;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by mich8 on 02-Oct-16.
 */
public class SongStructure {

    private Map<Double, String> songTimeToPart = new HashMap<>();

    public SongStructure(JsonArray jsonArray) {
        for (Object json : jsonArray) {
            JsonObject jsonObject = (JsonObject) json;
            double timeSec = jsonObject.getDouble("time") / 1000D;
            String tabText = jsonObject.getString("text");
            songTimeToPart.put(timeSec, tabText);
        }
    }

    public String getCurrentPart(double time) {
        //we want to find the start time of this part, so we need the latest start time that is still before the queried time
        Optional<Double> partTime = songTimeToPart.keySet()
                .stream()
                .filter(x -> x <= time)
                .sorted((t1, t2) -> (-1) * Double.compare(t1, t2))
                .findFirst();

        return partTime
                .map(x -> songTimeToPart.get(partTime.get()))
                .orElse("");
    }
}

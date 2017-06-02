package com.github.mich8bsp;

import com.github.mich8bsp.tabmapper.view.StatefulText;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.List;

/**
 * Created by mich8 on 12-Oct-16.
 */
public class Utils {

    public static String getSongUrl(String filePath) {
        return "file:///" + filePath.replace("\\", "/").replaceAll(" ", "%20");
    }

    public static String formatTime(Duration time) {
        int roundedSeconds = (int) time.toSeconds();
        int hours = roundedSeconds / (60 * 60);
        if (hours > 0) {
            roundedSeconds -= hours * 60 * 60;
        }
        int minutes = roundedSeconds / 60;
        int seconds = roundedSeconds - hours * 60 * 60
                - minutes * 60;
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    public static String formatTime(Duration elapsed, Duration duration) {
        String elapsedTimeStr = formatTime(elapsed);

        if (duration.greaterThan(Duration.ZERO)) {
            String durationTimeStr = formatTime(duration);
            return elapsedTimeStr + "/" + durationTimeStr;
        } else {
            return elapsedTimeStr;
        }
    }

    public static JsonArray toJson(List<StatefulText<Duration>> data) {
        JsonArray sections = new JsonArray();
        for (StatefulText<Duration> txt : data) {
            JsonObject songSection = new JsonObject();
            songSection.put("text", txt.getInitialText());
            songSection.put("time", txt.getState().toMillis());
            sections.add(songSection);
        }
        return sections;
    }

    public static Font getDefaultFont() {
        return Font.font("Courier New", 12);

    }

    public static String getFullTitle(JsonObject songJson) {
        return songJson.getString("artist") + " - " + songJson.getString("title");
    }
}

package com.github.mich8bsp.tabmapper;

import com.github.mich8bsp.Utils;
import com.github.mich8bsp.mediaplayer.MediaControl;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabMapperView {

    static Parent getMapperView(TabRawInput input) {
        TabMappedInput mappedInput = TabMapper.parseTab(input);
        MediaControl mediaControl = getMediaControl(Utils.getSongUrl(mappedInput.getAudioFile().getAbsolutePath()));
        List<StatefulButton<Duration>> songParts = createTaggableSongParts(mappedInput, mediaControl.getMediaPlayer());

        VBox box = new VBox(10);
        box.getChildren().add(mediaControl);
        box.getChildren().addAll(songParts);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> saveTaggedSong(songParts));
        box.getChildren().add(submitButton);
        return new ScrollPane(box);
    }

    private static void saveTaggedSong(List<StatefulButton<Duration>> buttonList) {
        //FIXME: to be implemented. for example store to db
    }

    private static List<StatefulButton<Duration>> createTaggableSongParts(TabMappedInput mappedInput, MediaPlayer mediaPlayer) {
        return mappedInput.getSongParts().stream()
                .map(part -> getPartText(part, mappedInput.getPartNameToPart().get(part)))
                .map(text -> createTaggableButton(text, mediaPlayer))
                .collect(Collectors.toList());
    }

    private static String getPartText(String partName, List<String> lines) {
        return "[" + partName + "]\n\n" + lines.stream().collect(Collectors.joining("\n"));
    }

    private static StatefulButton<Duration> createTaggableButton(String text, MediaPlayer mediaPlayer) {
        StatefulButton<Duration> button = new StatefulButton<>(text);
        button.setOnAction(e -> {
            Duration currentTimeTagged = mediaPlayer.getCurrentTime();
            button.setState(currentTimeTagged);
            //we use initialText here instead of text because button can be tagged multiple times
            button.setText("(" + Utils.formatTime(currentTimeTagged) + ") " + button.getInitialText());
        });
        return button;
    }

    private static MediaControl getMediaControl(String audioFilePath) {
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(audioFilePath));
        mediaPlayer.setAutoPlay(false);

        return new MediaControl(mediaPlayer);
    }
}

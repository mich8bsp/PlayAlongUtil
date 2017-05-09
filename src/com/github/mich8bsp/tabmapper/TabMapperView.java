package com.github.mich8bsp.tabmapper;

import com.github.mich8bsp.Utils;
import com.github.mich8bsp.mediaplayer.MediaControl;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabMapperView {

    public static Parent getMapperView(TabRawInput input){
        TabMappedInput mappedInput = TabMapper.parseTab(input);
        List<Button> buttonList = mappedInput.getSongParts().stream()
                .map(part -> mappedInput.getPartNameToPart().get(part))
                .map(lines -> lines.stream().collect(Collectors.joining("\n")))
                .map(Button::new)
                .collect(Collectors.toList());
        VBox box = new VBox(10);
        MediaControl mediaControl = getMediaControl(Utils.getSongUrl(mappedInput.getAudioFile().getAbsolutePath()));
        box.getChildren().add(mediaControl);
        box.getChildren().addAll(buttonList);
        return box;
    }

    public static MediaControl getMediaControl(String audioFilePath){
        Media songMedia = new Media(audioFilePath);
        MediaPlayer mediaPlayer = new MediaPlayer(songMedia);
        mediaPlayer.setAutoPlay(false);

        return new MediaControl(mediaPlayer);
    }
}

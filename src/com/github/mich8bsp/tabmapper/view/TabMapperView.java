package com.github.mich8bsp.tabmapper.view;

import com.github.mich8bsp.Utils;
import com.github.mich8bsp.mediaplayer.MediaControl;
import com.github.mich8bsp.tabmapper.songstructure.SongSection;
import com.github.mich8bsp.tabmapper.input.TabRawInput;
import com.github.mich8bsp.tabmapper.input.TabMappedInput;
import com.github.mich8bsp.tabmapper.parsing.TabMapper;
import javafx.geometry.Point2D;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabMapperView {

    public static Parent getMapperView(TabRawInput input) {
        TabMappedInput mappedInput = TabMapper.parseTab(input);
        MediaControl mediaControl = MediaControl.buildMediaControl(Utils.getSongUrl(mappedInput.getAudioFile().getAbsolutePath()));
        List<StatefulText<Duration>> songParts = createTaggableSongParts(mappedInput, mediaControl.getMediaPlayer());

        VBox box = new VBox(10);
        box.getChildren().add(mediaControl);
        box.getChildren().addAll(songParts);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> saveTaggedSong(songParts));
        box.getChildren().add(submitButton);
        return new ScrollPane(box);
    }

    private static void saveTaggedSong(List<StatefulText<Duration>> buttonList) {
        Duration lastTime = null;
        for(StatefulText<Duration> button : buttonList){
            if(button.getState()!=null){
                lastTime=button.getState();
            }else if(lastTime!=null){
                //if some of the parts were not tagged, we auto-tag them with nearest tagged neighbour that came before
                button.setState(lastTime);
            }
        }
        //FIXME: to be implemented. for example store to db
    }

    private static List<StatefulText<Duration>> createTaggableSongParts(TabMappedInput mappedInput, MediaPlayer mediaPlayer) {
        return mappedInput.getSongParts().stream()
                .map(part -> getPartText(part, mappedInput.getPartNameToPart().get(part)))
                .map(text -> createTaggableButton(text, mediaPlayer))
                .collect(Collectors.toList());
    }

    private static String getPartText(String partName, SongSection part) {
        return "[" + partName + "]\n\n"
                + part.getLines()
                .stream()
                .collect(Collectors.joining("\n"));
    }

    private static StatefulText<Duration> createTaggableButton(String text, MediaPlayer mediaPlayer) {

        StatefulText<Duration> clickableText = new StatefulText<>(text);

        clickableText.setOnMouseClicked(e -> {
            if(e.isControlDown()) {
                int index = (int) clickableText.queryAccessibleAttribute(AccessibleAttribute.OFFSET_AT_POINT,
                        new Point2D(e.getScreenX(), e.getScreenY()));
                System.out.println(index);
                //FIXME: implement dynamic splitting here
            }else{
                Duration currentTimeTagged = mediaPlayer.getCurrentTime();
                clickableText.setState(currentTimeTagged);
                //we use initialText here instead of text because button can be tagged multiple times
                clickableText.setText("(" + Utils.formatTime(currentTimeTagged) + ") " + clickableText.getInitialText());
            }
        });
        return clickableText;
    }

}

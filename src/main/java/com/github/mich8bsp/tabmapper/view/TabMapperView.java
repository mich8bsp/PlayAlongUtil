package com.github.mich8bsp.tabmapper.view;

import com.github.mich8bsp.Utils;
import com.github.mich8bsp.db.DBStoredTab;
import com.github.mich8bsp.mediaplayer.MediaControl;
import com.github.mich8bsp.tabmapper.input.TabInputForm;
import com.github.mich8bsp.tabmapper.input.TabMappedInput;
import com.github.mich8bsp.tabmapper.input.TabRawInput;
import com.github.mich8bsp.tabmapper.parsing.TabMapper;
import com.github.mich8bsp.tabmapper.songstructure.SongSection;
import com.github.mich8bsp.tabmapper.storage.DBStore;
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

    private static boolean editModeEnabled = false;
    private static TabInputForm.InputField editableTab = null;
    private static List<StatefulText<Duration>> songParts = null;
    private static DBStoredTab storedTab = null;

    private static void mapInput(TabRawInput input, MediaPlayer mediaPlayer){
        TabMappedInput mappedInput = TabMapper.parseTab(input);
        songParts = createTaggableSongParts(mappedInput, mediaPlayer);
        storedTab = new DBStoredTab(mappedInput, songParts);
    }

    public static Parent getMapperView(TabRawInput input) {
        MediaControl mediaControl = MediaControl.buildMediaControl(Utils.getSongUrl(input.getAudioFile().getAbsolutePath()));
        mapInput(input, mediaControl.getMediaPlayer());

        VBox box = new VBox(10);
        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            editModeEnabled = !editModeEnabled;
            editButton.setText(editModeEnabled ? "Finish editing" : "Edit");
            box.getChildren().clear();
            box.getChildren().add(mediaControl);
            box.getChildren().add(editButton);
            if(editModeEnabled){
                TabInputForm.InputField tabInputField = TabInputForm.createTabInputField();
                tabInputField.getTextField().setText(input.getTab());
                editableTab = tabInputField;
                box.getChildren().add(tabInputField);
            }
            if(!editModeEnabled) {
                if(editableTab!=null){
                    input.setTab(editableTab.getInputText());
                    mapInput(input, mediaControl.getMediaPlayer());
                    box.getChildren().addAll(songParts);
                }
                box.getChildren().add(getSubmitButton(storedTab));
            }
        });
        box.getChildren().add(mediaControl);
        box.getChildren().add(editButton);
        box.getChildren().addAll(songParts);
        box.getChildren().add(getSubmitButton(storedTab));

        return new ScrollPane(box);
    }

    private static Button getSubmitButton(DBStoredTab storedTab){
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> DBStore.storeToDB(storedTab));
        return submitButton;
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
            if (e.isControlDown()) {
                int index = (int) clickableText.queryAccessibleAttribute(AccessibleAttribute.OFFSET_AT_POINT,
                        new Point2D(e.getScreenX(), e.getScreenY()));
                System.out.println(index);
                //FIXME: implement dynamic splitting here
            } else {
                Duration currentTimeTagged = mediaPlayer.getCurrentTime();
                clickableText.setState(currentTimeTagged);
                //we use initialText here instead of text because button can be tagged multiple times
                clickableText.setText("(" + Utils.formatTime(currentTimeTagged) + ") " + clickableText.getInitialText());
            }
        });
        return clickableText;
    }

}

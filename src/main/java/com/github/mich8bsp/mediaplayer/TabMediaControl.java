package com.github.mich8bsp.mediaplayer;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;

import java.util.List;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabMediaControl extends MediaControl {
    private final SongStructure tabStructure;
    private SongStructure lyricsStructure;
    private ISongEvents songControls;
    private TextArea tabTextArea;
    private TextArea lyricsTextArea;
    private ToggleButton shuffleButton;

    public TabMediaControl(MediaPlayer mp, SongStructure tabStructure, SongStructure lyricsStructure,
                           ISongEvents songManager, ListView songListView) {
        super(mp);
        this.tabStructure = tabStructure;
        this.lyricsStructure = lyricsStructure;
        this.songControls = songManager;
        buildView(songListView);
    }

    @Override
    protected void buildPlaybackControls() {
        super.buildPlaybackControls();
        getMediaPlayer().setOnEndOfMedia(this::skipToNext);
    }

    private void buildView(ListView songsList) {
        BorderPane mvPane = new BorderPane();

        tabTextArea = new TextArea();
        tabTextArea.setFont(Font.font("Courier New", 12));
        tabTextArea.setText(tabStructure.getCurrentPart(0));
        mvPane.setCenter(tabTextArea);

        if (lyricsStructure != null) {
            lyricsTextArea = new TextArea();
            lyricsTextArea.setText(lyricsStructure.getCurrentPart(0));
            mvPane.setLeft(lyricsTextArea);
        }

        mvPane.setRight(songsList);
        setCenter(mvPane);
    }

    protected void skipToNext() {
       super.stop();
        if (songControls != null) {
            songControls.onSongChange();
        }
    }


    @Override
    protected void addAdditionalToMediaBar(HBox mediaBar){
        ImageView skipButtonImg = new ImageView(new Image("skip-button.png"));
        skipButtonImg.setFitHeight(20);
        skipButtonImg.setFitWidth(20);

        Button skipButton = new Button("", skipButtonImg);
        skipButton.setOnAction(event -> skipToNext());

        mediaBar.getChildren().add(skipButton);

        ImageView shuffleButtonImg = new ImageView(new Image("shuffle-button.png"));
        shuffleButtonImg.setFitHeight(20);
        shuffleButtonImg.setFitWidth(20);
        shuffleButton = new ToggleButton("", shuffleButtonImg);

        shuffleButton.setOnAction(event->{
            songControls.onShuffleChanged();
            shuffleButton.setSelected(songControls.isShuffleOn());
        });
        mediaBar.getChildren().add(shuffleButton);
    }

    @Override
    public void updateValues(){
        shuffleButton.setSelected(songControls.isShuffleOn());
        super.updateValues();
    }

    @Override
    public void updateAdditionalValues(double currentTime){
        tabTextArea.setText(tabStructure.getCurrentPart(currentTime));
        if (lyricsStructure != null) {
            lyricsTextArea.setText(lyricsStructure.getCurrentPart(currentTime));
        }
    }
}

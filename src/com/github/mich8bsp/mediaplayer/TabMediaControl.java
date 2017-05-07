package com.github.mich8bsp.mediaplayer;

import com.github.mich8bsp.ISongControls;
import com.github.mich8bsp.ISongEvents;
import com.github.mich8bsp.SongManager;
import com.github.mich8bsp.SongStructure;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabMediaControl extends MediaControl {
    private final SongStructure tabStructure;
    private SongStructure lyricsStructure;
    private ISongControls songControls;
    private TextArea tabTextArea;
    private TextArea lyricsTextArea;
    private ISongEvents songEventObserver;
    private ToggleButton shuffleButton;

    public TabMediaControl(MediaPlayer mp, SongStructure tabStructure, SongStructure lyricsStructure, SongManager songManager) {
        super(mp);
        this.tabStructure = tabStructure;
        this.lyricsStructure = lyricsStructure;
        this.songControls = songManager;
        buildView(songManager.getSongsList());

        setEventObserver(songManager);
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

    public void setEventObserver(ISongEvents observer) {
        songEventObserver = observer;
    }

    protected void skipToNext() {
       super.stop();
        if (songEventObserver != null) {
            songEventObserver.onSongChange();
        }
    }


    @Override
    protected void addAdditionalToMediaBar(HBox mediaBar){
        Button skipButton = new Button(">>");
        skipButton.setOnAction(event -> skipToNext());

        mediaBar.getChildren().add(skipButton);


        shuffleButton = new ToggleButton("RND");

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

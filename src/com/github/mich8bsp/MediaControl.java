package com.github.mich8bsp;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class MediaControl extends BorderPane {

    private final SongStructure tabStructure;
    private SongStructure lyricsStructure;
    private MediaPlayer mp;
    private TextArea tabTextArea;
    private TextArea lyricsTextArea;
    private Duration duration;
    private Slider timeSlider;
    private Label timeLabel;
    private Slider volumeSlider;
    private HBox mediaBar;
    private SongEvents songEventObserver;
    private SongControls songControls;
    private ToggleButton shuffleButton;

    public MediaControl(MediaPlayer mp, SongStructure tabStructure, SongStructure lyricsStructure, SongManager songManager) {
        this.mp = mp;
        this.tabStructure = tabStructure;
        this.lyricsStructure = lyricsStructure;
        this.songControls = songManager;
        buildView(songManager.getSongsList());
        buildControls();

        setEventObserver(songManager);
    }

    private void buildControls() {
        mediaBar = new HBox();
        mediaBar.setAlignment(Pos.CENTER);
        mediaBar.setPadding(new Insets(5, 10, 5, 10));
        BorderPane.setAlignment(mediaBar, Pos.CENTER);
        buildPlaybackControls();
        buildTimeControls();
        buildVolumeControls();
        setBottom(mediaBar);
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

    private void buildPlaybackControls() {
        final Button playButton = new Button(">");

        playButton.setOnAction(e -> {
            Status status = mp.getStatus();

            if (status == Status.UNKNOWN || status == Status.HALTED) {
                // don't do anything in these states
            } else if (status == Status.PAUSED
                    || status == Status.READY
                    || status == Status.STOPPED) {
                mp.play();
            } else {
                mp.pause();
            }
        });

        mp.setOnPlaying(() -> playButton.setText("||"));

        mp.setOnPaused(() -> playButton.setText(">"));

        mp.setOnReady(() -> {
            duration = mp.getMedia().getDuration();
            updateValues();
        });

        mp.setOnEndOfMedia(this::skipToNext);

        mediaBar.getChildren().add(playButton);

        Button skipButton = new Button(">>");
        skipButton.setOnAction(event -> skipToNext());

        mediaBar.getChildren().add(skipButton);

        shuffleButton = new ToggleButton("RND");

        shuffleButton.setOnAction(event->{
            songControls.onShuffleChanged();
            shuffleButton.setSelected(songControls.isShuffleOn());
        });
        mediaBar.getChildren().add(shuffleButton);
        // Add spacer
        Label spacer = new Label("   ");
        mediaBar.getChildren().add(spacer);

    }

    private void buildTimeControls() {
        // Add Time label
        Label timeLabel = new Label("Time: ");
        mediaBar.getChildren().add(timeLabel);

        // Add time slider
        timeSlider = new Slider();
        HBox.setHgrow(timeSlider, Priority.ALWAYS);
        timeSlider.setMinWidth(50);
        timeSlider.setMaxWidth(Double.MAX_VALUE);
        timeSlider.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (timeSlider.isValueChanging()) {
                // multiply duration by percentage calculated by slider position
                mp.seek(duration.multiply(newValue.doubleValue() / 100.0));
            }
        });
        mediaBar.getChildren().add(timeSlider);

        // Add Play label
        this.timeLabel = new Label();
        this.timeLabel.setPrefWidth(130);
        this.timeLabel.setMinWidth(50);
        mediaBar.getChildren().add(this.timeLabel);
    }

    private void buildVolumeControls() {
        // Add the volume label
        Label volumeLabel = new Label("Vol: ");
        mediaBar.getChildren().add(volumeLabel);

        // Add Volume slider
        volumeSlider = new Slider();
        volumeSlider.setPrefWidth(70);
        volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
        volumeSlider.setMinWidth(30);
        volumeSlider.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (volumeSlider.isValueChanging()) {
                mp.setVolume(newValue.doubleValue() / 100.0);
            }
        });
        mediaBar.getChildren().add(volumeSlider);
    }

    protected void updateValues() {
        shuffleButton.setSelected(songControls.isShuffleOn());
        if (timeLabel != null && timeSlider != null && volumeSlider != null) {
            Platform.runLater(() -> {
                Duration currentTime = mp.getCurrentTime();
                timeLabel.setText(Utils.formatTime(currentTime, duration));
                timeSlider.setDisable(duration.isUnknown());
                if (!timeSlider.isDisabled()
                        && duration.greaterThan(Duration.ZERO)
                        && !timeSlider.isValueChanging()) {
                    timeSlider.setValue(currentTime.divide(duration.toMillis()).toMillis()
                            * 100.0);
                    tabTextArea.setText(tabStructure.getCurrentPart(currentTime.toSeconds()));
                    if (lyricsStructure != null) {
                        lyricsTextArea.setText(lyricsStructure.getCurrentPart(currentTime.toSeconds()));
                    }
                }
                if (!volumeSlider.isValueChanging()) {
                    volumeSlider.setValue(Math.round(mp.getVolume()
                            * 100));
                }
            });
        }
    }

    public void setEventObserver(SongEvents observer) {
        songEventObserver = observer;
    }

    private void skipToNext() {
        mp.stop();
        if (songEventObserver != null) {
            songEventObserver.onSongChange();
        }
    }
}
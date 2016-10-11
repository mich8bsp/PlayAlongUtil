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
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.List;

public class MediaControl extends BorderPane {

    private final SongStructure tabStructure;
    private SongStructure lyricsStructure;
    private MediaPlayer mp;
    private TextArea tabTextField;
    private TextArea lyricsTextField;
    private Duration duration;
    private Slider timeSlider;
    private Label playTime;
    private Slider volumeSlider;
    private HBox mediaBar;

    private List<SongEvents> songEventObservers = new LinkedList<>();

    public MediaControl(MediaPlayer mp, SongStructure tabStructure, SongStructure lyricsStructure, SongManager songManager) {
        this.mp = mp;
        this.tabStructure = tabStructure;
        this.lyricsStructure = lyricsStructure;

        buildView(songManager.getSongsList());
        buildPlaybackControls();
        buildTimeControls();
        buildVolumeControls();
        setBottom(mediaBar);
    }

    private void buildView(ListView songsList) {
        BorderPane mvPane = new BorderPane();

        tabTextField = new TextArea();
        tabTextField.setText(tabStructure.getCurrentPart(0));
        tabTextField.setWrapText(true);
        mvPane.setLeft(tabTextField);

        if(lyricsStructure!=null) {
            lyricsTextField = new TextArea();
            lyricsTextField.setText(lyricsStructure.getCurrentPart(0));
            lyricsTextField.setWrapText(true);
            mvPane.setCenter(lyricsTextField);
        }

        mvPane.setRight(songsList);

        setCenter(mvPane);

        mediaBar = new HBox();
        mediaBar.setAlignment(Pos.CENTER);
        mediaBar.setPadding(new Insets(5, 10, 5, 10));
        BorderPane.setAlignment(mediaBar, Pos.CENTER);

    }

    private void buildPlaybackControls(){
        final Button playButton = new Button(">");

        playButton.setOnAction(e -> {
            Status status = mp.getStatus();

            if (status == Status.UNKNOWN || status == Status.HALTED) {
                // don't do anything in these states
            }else if (status == Status.PAUSED
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
        // Add spacer
        Label spacer = new Label("   ");
        mediaBar.getChildren().add(spacer);

    }

    private void buildTimeControls(){
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
                mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
            }
        });
        mediaBar.getChildren().add(timeSlider);

        // Add Play label
        playTime = new Label();
        playTime.setPrefWidth(130);
        playTime.setMinWidth(50);
        mediaBar.getChildren().add(playTime);
    }

    private void buildVolumeControls(){
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
                mp.setVolume(volumeSlider.getValue() / 100.0);
            }
        });
        mediaBar.getChildren().add(volumeSlider);
    }

    protected void updateValues() {
        if (playTime != null && timeSlider != null && volumeSlider != null) {
            Platform.runLater(() -> {
                Duration currentTime = mp.getCurrentTime();
                playTime.setText(formatTime(currentTime, duration));
                timeSlider.setDisable(duration.isUnknown());
                if (!timeSlider.isDisabled()
                        && duration.greaterThan(Duration.ZERO)
                        && !timeSlider.isValueChanging()) {
                    timeSlider.setValue(currentTime.divide(duration.toMillis()).toMillis()
                            * 100.0);
                    tabTextField.setText(tabStructure.getCurrentPart(currentTime.toSeconds()));
                    if(lyricsStructure!=null) {
                        lyricsTextField.setText(lyricsStructure.getCurrentPart(currentTime.toSeconds()));
                    }
                }
                if (!volumeSlider.isValueChanging()) {
                    volumeSlider.setValue((int) Math.round(mp.getVolume()
                            * 100));
                }
            });
        }
    }

    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) elapsed.toSeconds();
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) duration.toSeconds();
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }

    public void addEventObserver(SongEvents observer) {
        songEventObservers.add(observer);
    }

    public List<SongEvents> getSongEventObservers() {
        return songEventObservers;
    }

    private void skipToNext(){
        mp.stop();
        songEventObservers.forEach(SongEvents::onSongChange);
        songEventObservers.clear();
    }
}
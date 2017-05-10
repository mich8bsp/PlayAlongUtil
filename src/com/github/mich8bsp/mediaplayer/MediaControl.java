package com.github.mich8bsp.mediaplayer;

import com.github.mich8bsp.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class MediaControl extends BorderPane {


    private MediaPlayer mp;

    private Duration duration;
    private Slider timeSlider;
    private Label timeLabel;
    private Slider volumeSlider;
    private HBox mediaBar;

    public MediaControl(MediaPlayer mp) {
        this.mp = mp;
        buildControls();
    }

    public MediaPlayer getMediaPlayer(){
        return mp;
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

    protected void buildPlaybackControls() {
        ImageView playButtonImg = new ImageView(new Image("play-button.png"));
        playButtonImg.setFitHeight(20);
        playButtonImg.setFitWidth(20);
        final Button playButton = new Button("", playButtonImg);

        ImageView pauseButtonImg = new ImageView(new Image("pause-button.png"));
        pauseButtonImg.setFitHeight(20);
        pauseButtonImg.setFitWidth(20);



        playButton.setOnAction(e -> {
            Status status = mp.getStatus();

            if (status == Status.UNKNOWN || status == Status.HALTED) {
                // don't do anything in these states
            } else if (status == Status.PAUSED
                    || status == Status.READY
                    || status == Status.STOPPED) {
                mp.play();
                mp.currentTimeProperty().addListener(ov -> updateValues());

            } else {
                mp.pause();
            }
        });

        mp.setOnPlaying(() -> playButton.setGraphic(pauseButtonImg));

        mp.setOnPaused(() -> playButton.setGraphic(playButtonImg));

        mp.setOnStopped(() -> playButton.setGraphic(playButtonImg));

        mp.setOnReady(() -> {
            duration = mp.getMedia().getDuration();
            updateValues();
        });

        mp.setOnEndOfMedia(this::stop);

        mediaBar.getChildren().add(playButton);

        ImageView stopButtonImg = new ImageView(new Image("stop-button.png"));
        stopButtonImg.setFitHeight(20);
        stopButtonImg.setFitWidth(20);
        final Button stopButton = new Button("", stopButtonImg);

        stopButton.setOnAction(event -> stop());

        mediaBar.getChildren().add(stopButton);

        addAdditionalToMediaBar(mediaBar);
        // Add spacer
        Label spacer = new Label("   ");
        mediaBar.getChildren().add(spacer);

    }

    protected void stop() {
        mp.stop();
        updateValues();
    }

    protected void addAdditionalToMediaBar(HBox mediaBar){

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

    public void updateValues() {
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
                    updateAdditionalValues(currentTime.toSeconds());
                }
                if (!volumeSlider.isValueChanging()) {
                    volumeSlider.setValue(Math.round(mp.getVolume()
                            * 100));
                }
            });
        }
    }

    public void updateAdditionalValues(double time) {

    }


}
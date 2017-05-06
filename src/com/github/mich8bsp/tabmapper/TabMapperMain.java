package com.github.mich8bsp.tabmapper;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.function.Consumer;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabMapperMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Tab Mapper");
        primaryStage.setMaximized(true);

        Scene mainScene = new Scene(new Group());

        Consumer<TabRawInput> onSubmit = input -> mainScene.setRoot(TabMapper.getMapperView(input));
        mainScene.setRoot(TabInputForm.getInputForm(onSubmit));

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

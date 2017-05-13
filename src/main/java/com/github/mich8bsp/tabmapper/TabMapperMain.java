package com.github.mich8bsp.tabmapper;

import com.github.mich8bsp.tabmapper.input.TabInputForm;
import com.github.mich8bsp.tabmapper.input.TabRawInput;
import com.github.mich8bsp.tabmapper.view.TabMapperView;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
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

        // when all user input is received and submit is clicked, we close the input form and open the mapper view
        Consumer<TabRawInput> onSubmit = input -> mainScene.setRoot(TabMapperView.getMapperView(input));

        //open input form on start
        mainScene.setRoot(TabInputForm.getInputForm(onSubmit));

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

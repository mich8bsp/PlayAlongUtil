package com.github.mich8bsp.tabmapper;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabInputForm {

    public static Parent getInputForm(Consumer<TabRawInput> onSubmit){

        Label songTitle = new Label("Title:");
        TextField titleTextField = new TextField();
        HBox titleHb = new HBox();
        titleHb.getChildren().addAll(songTitle, titleTextField);
        titleHb.setSpacing(10);

        Label artist = new Label("Artist:");
        TextField artistTextField = new TextField();
        HBox artistHb = new HBox();
        artistHb.getChildren().addAll(artist, artistTextField);
        artistHb.setSpacing(10);

        Label tabLabel = new Label("Tab:");
        TextArea tabTextField = new TextArea();

        HBox tabHb = new HBox();
        tabHb.getChildren().addAll(tabLabel, tabTextField);
        tabHb.setSpacing(10);

        Button submit = new Button("Submit");

        submit.setOnAction(event->{
            if(titleTextField.getText().isEmpty()){
                System.out.println("Missing title field");
                return;
            }
            if(artistTextField.getText().isEmpty()){
                System.out.println("Missing artist field");
                return;
            }
            if(tabTextField.getText().isEmpty()){
                System.out.println("Missing tabs");
                return;
            }
            TabRawInput tabInputForm = new TabRawInput(titleTextField.getText(), artistTextField.getText(), tabTextField.getText());
            onSubmit.accept(tabInputForm);
        });

        VBox verticalContainer = new VBox();
        verticalContainer.getChildren().addAll(titleHb, artistHb, tabHb, submit);
        verticalContainer.setSpacing(30);

        Group root = new Group();
        root.getChildren().addAll(verticalContainer);
        return root;
    }
}

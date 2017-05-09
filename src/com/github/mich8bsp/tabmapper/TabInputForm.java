package com.github.mich8bsp.tabmapper;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabInputForm {

    public static class InputField extends HBox{
        private TextInputControl textField;

        public InputField(String label){
            this(label, TextField::new);
        }

        public InputField(String label, Supplier<TextInputControl> inputFieldSupplier){
            textField = inputFieldSupplier.get();
            getChildren().addAll(new Label(label), textField);
            setSpacing(10);
        }

        public String getInputText(){
            return textField.getText();
        }

    }

    public static Parent getInputForm(Consumer<TabRawInput> onSubmit){

        InputField title = new InputField("Title:");
        InputField artist = new InputField("Artist:");
        InputField tab = new InputField("Tab:", TextArea::new);

        FileChooser fileChooser = new FileChooser();
        final Button addAudioButton = new Button("Add audio");

        final File[] audioFile = new File[1];
        addAudioButton.setOnAction(e -> audioFile[0] = fileChooser.showOpenDialog(new Stage()));

        Button submit = new Button("Submit");

        submit.setOnAction(event->{
            String inputTitle = title.getInputText();
            String inputArtist = artist.getInputText();
            String inputTab = tab.getInputText();
            if(inputTitle.isEmpty()){
                System.out.println("Missing title field");
                return;
            }
            if(inputArtist.isEmpty()){
                System.out.println("Missing artist field");
                return;
            }
            if(inputTab.isEmpty()){
                System.out.println("Missing tabs");
                return;
            }
            TabRawInput tabInputForm = new TabRawInput(inputTitle, inputArtist, inputTab, audioFile[0]);
            onSubmit.accept(tabInputForm);
        });

        VBox verticalContainer = new VBox();
        verticalContainer.getChildren().addAll(title, artist, tab, addAudioButton, submit);
        verticalContainer.setSpacing(30);

        Group root = new Group();
        root.getChildren().addAll(verticalContainer);
        return root;
    }
}

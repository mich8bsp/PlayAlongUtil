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

        HBox audioInput = new HBox();
        audioInput.setSpacing(10);
        StatefulButton<File> audioSelectButton = new StatefulButton<>("Add audio");
        Label chosenFileLabel = new Label();
        audioInput.getChildren().addAll(audioSelectButton, chosenFileLabel);

        audioSelectButton.setOnAction(e -> {
            File audioFile = fileChooser.showOpenDialog(new Stage());
            audioSelectButton.setState(audioFile);
            chosenFileLabel.setText(audioFile.getName());
        });

        Button submit = new Button("Submit");

        Label errors = new Label();

        submit.setOnAction(event->{
            String inputTitle = title.getInputText();
            String inputArtist = artist.getInputText();
            String inputTab = tab.getInputText();
            errors.setText("");
            if(inputTitle.isEmpty()){
                errors.setText(errors.getText() + "Missing title field\n");
            }
            if(inputArtist.isEmpty()){
                errors.setText(errors.getText() + "Missing artist field\n");
            }
            if(inputTab.isEmpty()){
                errors.setText(errors.getText() + "Missing tabs\n");
            }
            if(audioSelectButton.getState()==null){
                errors.setText(errors.getText() + "Missing audio file\n");
            }
            if(!errors.getText().isEmpty()){
                return;
            }
            TabRawInput tabInputForm = new TabRawInput(inputTitle, inputArtist, inputTab, audioSelectButton.getState());
            onSubmit.accept(tabInputForm);
        });

        VBox verticalContainer = new VBox();
        verticalContainer.getChildren().addAll(title, artist, tab, audioInput, submit, errors);
        verticalContainer.setSpacing(30);

        Group root = new Group();
        root.getChildren().addAll(verticalContainer);
        return root;
    }
}

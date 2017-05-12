package com.github.mich8bsp.tabmapper;

import javafx.scene.text.Text;

/**
 * Created by mich8 on 11-May-17.
 */
public class StatefulText<T> extends Text{
    private T state;

    //because we change the text of this button by prepending the state, we want to store the original text
    private String initialText;

    public StatefulText(String text) {
        super(text);
        this.initialText = text;
    }

    public T getState() {
        return state;
    }

    public void setState(T state) {
        this.state = state;
    }

    public String getInitialText() {
        return initialText;
    }

    public void setInitialText(String initialText){
        this.initialText = initialText;
    }
}

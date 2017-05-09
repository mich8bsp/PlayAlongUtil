package com.github.mich8bsp.tabmapper;

import javafx.scene.control.Button;

/**
 * Created by mich8 on 09-May-17.
 */
public class StatefulButton<T> extends Button {
    private T state;
    private String initialText;

    public StatefulButton(String text) {
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
}

package com.github.mich8bsp.tabmapper;

import javafx.scene.Parent;
import javafx.scene.control.Label;

/**
 * Created by mich8 on 06-May-17.
 */
public class TabMapperView {

    public static Parent getMapperView(TabRawInput input){
        TabMapper.parseTab(input);
        //TODO: use parsed tab to show taggable view
        return new Label();
    }
}

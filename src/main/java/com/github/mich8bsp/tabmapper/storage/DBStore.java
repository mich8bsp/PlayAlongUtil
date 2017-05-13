package com.github.mich8bsp.tabmapper.storage;

import com.github.mich8bsp.tabmapper.view.StatefulText;
import javafx.util.Duration;

import java.util.List;

/**
 * Created by mich8 on 13-May-17.
 */
public class DBStore {

    private static void preprocessData(List<StatefulText<Duration>> mappedData){
        Duration lastTime = null;
        for(StatefulText<Duration> data : mappedData){
            if(data.getState()!=null){
                lastTime=data.getState();
            }else if(lastTime!=null){
                //if some of the parts were not tagged, we auto-tag them with nearest tagged neighbour that came before
                data.setState(lastTime);
            }
        }
    }

    public static void storeToDB(List<StatefulText<Duration>> mappedData){
        preprocessData(mappedData);


    }
}

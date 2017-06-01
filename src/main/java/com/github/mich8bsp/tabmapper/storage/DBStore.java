package com.github.mich8bsp.tabmapper.storage;

import com.github.mich8bsp.db.DBConn;
import com.github.mich8bsp.db.DBStoredTab;
import com.github.mich8bsp.tabmapper.view.StatefulText;
import javafx.util.Duration;

import java.util.List;

/**
 * Created by mich8 on 13-May-17.
 */
public class DBStore {

    private static void preprocessData(List<StatefulText<Duration>> mappedData){
        Duration lastTime = null;
        if(mappedData.isEmpty()){
            return;
        }
        if(mappedData.get(0).getState()==null){
            mappedData.get(0).setState(new Duration(0));
        }

        for(StatefulText<Duration> data : mappedData){
            if(data.getState()!=null){
                lastTime=data.getState();
            }else if(lastTime!=null){
                //if some of the parts were not tagged, we auto-tag them with nearest tagged neighbour that came before
                data.setState(lastTime);
            }
        }
    }

    public static void storeToDB(DBStoredTab storedTab){
        preprocessData(storedTab.getMappedSections());

        DBConn.getDBClient().insert(DBConn.COLLECTION_NAME, storedTab.toJson(), res->{
            if(res.failed()){
                System.out.println("Submit failed with cause " + res.cause().getMessage());
            }else{
                System.out.println("Stored successfully");
            }
        });

    }



}

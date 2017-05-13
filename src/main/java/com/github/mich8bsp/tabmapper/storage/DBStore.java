package com.github.mich8bsp.tabmapper.storage;

import com.github.mich8bsp.tabmapper.view.StatefulText;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by mich8 on 13-May-17.
 */
public class DBStore {

    private static Vertx vertx = Vertx.vertx();
    private static MongoClient mongoClient;

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
        if(mongoClient==null){
            initMongoClient();
        }
        mongoClient.insert("guitar-tabs", storedTab.toJson(), res->{
            if(res.failed()){
                System.out.println("Submit failed with cause " + res.cause().getMessage());
            }else{
                System.out.println("Stored successfully");
            }
        });

    }

    private static void initMongoClient() {
        JsonObject config = new JsonObject();
        config.put("dbHost", "localhost");
        config.put("port", 27017);
        config.put("username", "guest");
        config.put("password", "guest");
        config.put("dbName", "test");
        try {
            String configJson = new String(Files.readAllBytes(Paths.get("src/main/resources/login.conf")));
            config = new JsonObject(configJson);
        } catch (IOException e) {
            System.out.println("Specify db information in login.conf file in resources folder");
        }

        mongoClient = MongoClient.createShared(vertx, config);
    }

    public static void main(String[] args) {
        initMongoClient();
    }
}

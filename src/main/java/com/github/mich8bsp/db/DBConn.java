package com.github.mich8bsp.db;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Michael Bespalov on 01-Jun-17.
 */
public class DBConn {

    public static final String COLLECTION_NAME = "guitar-tabs";

    private static Vertx vertx = Vertx.vertx();
    private static MongoClient mongoClient;


    public static MongoClient getDBClient() {
        if(mongoClient!=null){
            return mongoClient;
        }
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
        return mongoClient;
    }
}

package com.annosearch.storage;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 25-Jan-2017
 */
public class MapDBStorage {

    private static final Logger LOG = LoggerFactory.getLogger(MapDBStorage.class);

    private DB.HashMapMaker<Integer, String> dbMaker;

    public static MapDBStorage newInstance(String dbName, String mapName) {
        return new MapDBStorage(dbName, mapName);
    }

    private MapDBStorage(String dbName, String mapName) {
        try {
            Path path = Paths.get(dbName);
            Files.createDirectories(path.getParent());
//            Files.createFile(path);
        } catch (IOException e) {
            LOG.error("Error creating {} storage file. ", dbName, e);
        }
        initDB(dbName, mapName);
    }

    private void initDB(String dbName, String mapName) {
        DB db = DBMaker
                .fileDB(dbName)
                .fileMmapEnableIfSupported() // Only enable mmap on supported platforms
                .transactionEnable()
                .closeOnJvmShutdown()
                .make();

        this.dbMaker = db
                .hashMap(mapName, Serializer.INTEGER, Serializer.STRING);
    }

    public void save(Map<Integer, String> map) {
        try (HTreeMap<Integer, String> persistentMap = this.dbMaker.createOrOpen()) {
            persistentMap.putAll(map);
        }
    }

}

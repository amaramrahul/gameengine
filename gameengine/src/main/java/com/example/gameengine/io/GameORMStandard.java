package com.example.gameengine.io;

import com.aerospike.client.*;
import com.aerospike.client.policy.GenerationPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.example.gameengine.game.GameBean;

import java.util.List;
import java.util.Map;

/**
 * Created by rahul on 23/2/16.
 */
public class GameORMStandard implements GameORM {
    private static AerospikeClient aerospikeClient = new AerospikeClient("127.0.0.1", 3000);
    private int expiration;
    private int generation;
    private final int gameId;
    private GameBean gameobj = null;

    public GameORMStandard(Integer gameId) {
        this.gameId = gameId;
    }

    public GameBean getCached() {
        return (gameobj != null)?gameobj:get();
    }

    public GameBean get() {
        // Read the record from database
        WritePolicy policy = new WritePolicy();
        Key gameKey = new Key("game", "", gameId);
        Record record = aerospikeClient.get(policy, gameKey);

        // Build the game bean from the returned record
        GameBean gameobj = new GameBean();
        gameobj.setGameId(record.getInt("gameId"));
        gameobj.setGameName(record.getString("gameName"));
        gameobj.setGroupId(record.getInt("groupId"));
        gameobj.setGroupVersion(record.getInt("groupVersion"));
        // sanitize some stack input
        for (Map<Object, Object> state: (List<Map<Object, Object>>) record.getList("stateStack")) {
            state.put("version", ((Long) state.get("version")).intValue());
            state.put("isActive", ((Long) state.get("isActive")).intValue());
        }
        gameobj.setStateStack((List<Map<Object, Object>>) record.getList("stateStack"));

        // Cache the result
        this.gameobj = gameobj;
        expiration = record.expiration;
        generation = record.generation;

        return gameobj;
    }

    public void set(GameBean gameobj) {
        // Write to db
        WritePolicy policy = new WritePolicy();
        policy.generation = generation;
        policy.generationPolicy = GenerationPolicy.EXPECT_GEN_EQUAL;
        Key gameKey = new Key("game", "", gameId);
        Bin gameIdBin = new Bin("gameId", gameobj.getGameId());
        Bin gameNameBin = new Bin("gameName", gameobj.getGameName());
        Bin groupIdBin = new Bin("groupId", gameobj.getGroupId());
        Bin groupVersionBin = new Bin("groupVersion", gameobj.getGroupVersion());
        Bin stateStackBin = new Bin("stateStack", gameobj.getStateStack());
        Record record = aerospikeClient.operate(policy, gameKey, Operation.put(gameIdBin),
                Operation.put(gameNameBin), Operation.put(groupIdBin), Operation.put(groupVersionBin),
                Operation.put(stateStackBin), Operation.getHeader());

        // Cache if successful
        expiration = record.expiration;
        generation = record.generation;
        this.gameobj = gameobj;
    }
}

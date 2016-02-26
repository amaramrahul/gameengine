package com.example.gameengine.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rahul on 23/2/16.
 */
public class GameBean {
    private Integer gameId;
    private Integer groupId;
    private Integer groupVersion;
    private String gameName;
    private List<Map<Object, Object>> stateStack = new ArrayList<>();

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupVersion(Integer groupVersion) {
        this.groupVersion = groupVersion;
    }

    public Integer getGroupVersion() {
        return groupVersion;
    }

    public void setStateStack(List<Map<Object, Object>> stateStack) {
        this.stateStack = stateStack;
    }

    public List<Map<Object, Object>> getStateStack() {
        return stateStack;
    }
}

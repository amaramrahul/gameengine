package com.example.gameengine.game;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rahul on 23/2/16.
 */
public abstract class GameNewEvent implements GameEvent {

    protected Game game = null;

    public GameNewEvent(Game game) {
        this.game = game;
    }

    protected abstract Object updateData(Integer userId, Map<String, Object> params);

    protected Integer isActive(Integer userId, Map<String, Object> params) {
        return 1;
    }

    protected Integer getNextPlayer(Integer userId, Map<String, Object> params) throws IOException {
        return userId;
    }

    public void updateState(Integer userId, Map<String, Object> params) throws IOException {
        Map<Object, Object> state = new HashMap<>();
        state.put("data", updateData(userId, params));
        state.put("player", userId);
        state.put("action", null);
        state.put("nextPlayer", getNextPlayer(userId, params));
        state.put("version", 1);
        state.put("isActive", isActive(userId, params));
        game.setState(state);
    }
}

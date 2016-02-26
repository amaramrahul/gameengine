package com.example.gameengine.game;

import com.example.gameengine.exception.InvalidGameMove;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rahul on 23/2/16.
 */
public abstract class GameMoveEvent implements GameEvent {

    protected Game game = null;

    public GameMoveEvent(Game game) {
        this.game = game;
    }

    protected abstract Object updateData(Integer userId, Map<String, Object> params);

    protected Integer getNextPlayer(Integer userId, Map<String, Object> params) throws IOException {
        List<Integer> userIds = game.getUserIds();
        int currPlayerIndex = userIds.indexOf(Integer.parseInt(game.getState().get("nextPlayer").toString()));
        return userIds.get((currPlayerIndex+1)%userIds.size());
    }

    protected abstract Integer isActive(Integer userId, Map<String, Object> params);

    protected void validate(Integer userId, Map<String, Object> params) throws InvalidGameMove {
        Map<Object, Object> currState = game.getState();
        Integer nextPlayer = Integer.parseInt(currState.get("nextPlayer").toString());
        if ((Integer) currState.get("isActive") == 0) {
            throw new InvalidGameMove("Game is no longer active");
        }
        if (userId != nextPlayer) {
            throw new InvalidGameMove("Next move is expected by userId " + nextPlayer);
        }
        Integer expectedVersion = Integer.parseInt(currState.get("version").toString());
        Integer gotVersion = (Integer) params.get("version");
        if (expectedVersion != gotVersion) {
            throw new InvalidGameMove("Expecting version " + expectedVersion + " but got " + gotVersion);
        }
    }

    public void updateState(Integer userId, Map<String, Object> params) throws IOException, InvalidGameMove {
        validate(userId, params);
        Map<Object, Object> currState = game.getState();
        Map<Object, Object> newState = new HashMap<>();
        newState.put("data", updateData(userId, params));
        newState.put("player", userId);
        newState.put("action", params.get("action"));
        newState.put("nextPlayer", getNextPlayer(userId, params));
        newState.put("version", 1 + (Integer) currState.get("version"));
        newState.put("isActive", isActive(userId, params));
        game.setState(newState);
    }
}

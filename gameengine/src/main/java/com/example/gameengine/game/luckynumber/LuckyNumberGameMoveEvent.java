package com.example.gameengine.game.luckynumber;

import com.example.gameengine.game.Game;
import com.example.gameengine.game.GameMoveEvent;

import java.util.Map;

/**
 * Created by rahul on 23/2/16.
 */
public class LuckyNumberGameMoveEvent extends GameMoveEvent {

    public LuckyNumberGameMoveEvent(Game game) {
        super(game);
    }

    protected Object updateData(Integer userId, Map<String, Object> params) {
        // there is no data update for this game. So, we can return the older
        // state
        Map<Object, Object> currState = game.getState();
        return currState.get("data");
    }

    protected Integer isActive(Integer userId, Map<String, Object> params) {
        Map<Object, Object> currState = game.getState();
        return ((Integer) params.get("action") != Integer.parseInt(currState.get("data").toString())) ?
                1 : 0;
    }
}

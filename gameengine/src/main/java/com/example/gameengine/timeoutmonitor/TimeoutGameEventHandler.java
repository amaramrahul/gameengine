package com.example.gameengine.timeoutmonitor;

import com.example.gameengine.exception.InvalidGameMove;
import com.example.gameengine.game.Game;
import com.example.gameengine.game.GameFactory;
import com.example.gameengine.game.observers.NotifyUsersObserver;
import com.example.gameengine.game.observers.TimeoutObserver;

import java.io.IOException;
import java.util.Map;

/**
 * Created by rahul on 24/2/16.
 */
public class TimeoutGameEventHandler {
    public void processRequest(Map<String, Object> params) throws IOException {
        Game game = GameFactory.getGame((Integer) params.get("id"));
        if ((Integer) game.getState().get("version") != (Integer) params.get("version")) {
            return;
        }
        game.addObserver(new NotifyUsersObserver());
        game.addObserver(new TimeoutObserver());
        try {
            game.timeoutGame(0, params);
        } catch(InvalidGameMove e) {
            e.printStackTrace();
        }
    }
}

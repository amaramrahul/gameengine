package com.example.gameengine.game.luckynumber;

import com.example.gameengine.game.Game;
import com.example.gameengine.game.GameNewEvent;

import java.util.Map;

/**
 * Created by rahul on 23/2/16.
 */
public class LuckyNumberGameNewEvent extends GameNewEvent {

    public LuckyNumberGameNewEvent(Game game) {
        super(game);
    }

    protected Object updateData(Integer userId, Map<String, Object> params) {
        return 1 + (int)Math.floor(Math.random() * 10);
    }
}

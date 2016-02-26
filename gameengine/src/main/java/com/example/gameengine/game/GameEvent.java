package com.example.gameengine.game;

import com.example.gameengine.exception.InvalidGameMove;

import java.io.IOException;
import java.util.Map;

/**
 * Created by rahul on 23/2/16.
 */
public interface GameEvent {
    int NEW_EVENT = 0;
    int MOVE_EVENT = 1;
    int TIMEOUT_EVENT = 2;

    void updateState(Integer userId, Map<String, Object> params) throws IOException,InvalidGameMove;
}

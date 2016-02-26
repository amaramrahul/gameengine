package com.example.gameengine.io;

import com.example.gameengine.game.GameBean;

/**
 * Created by rahul on 24/2/16.
 */
public interface GameORM {
    void set(GameBean gameobj);
    GameBean get();
    GameBean getCached();
}

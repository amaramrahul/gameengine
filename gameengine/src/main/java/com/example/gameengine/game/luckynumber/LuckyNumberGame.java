package com.example.gameengine.game.luckynumber;

import com.example.gameengine.game.Game;
import com.example.gameengine.game.GameEvent;
import com.example.gameengine.io.GameORM;

/**
 * Created by rahul on 23/2/16.
 */
public class LuckyNumberGame extends Game {
    public LuckyNumberGame() {
        super();
    }
    public LuckyNumberGame(GameORM gameORM) {
        super(gameORM);
    }
    protected GameEvent getGameEvent(Integer stateId) {
        GameEvent gameEvent = null;
        if (stateId == GameEvent.NEW_EVENT) {
            gameEvent = new LuckyNumberGameNewEvent(this);
        } else if (stateId == GameEvent.MOVE_EVENT) {
            gameEvent = new LuckyNumberGameMoveEvent(this);
        } else if (stateId == GameEvent.TIMEOUT_EVENT) {
            gameEvent = new LuckyNumberGameTimeoutEvent(this);
        }

        return gameEvent;
    }

    public String getGameName() {
        return "LuckyNumber";
    }

    public Integer getTimeoutInterval() {
        return 30;
    }
}

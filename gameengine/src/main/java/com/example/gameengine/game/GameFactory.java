package com.example.gameengine.game;

import com.example.gameengine.game.luckynumber.LuckyNumberGame;
import com.example.gameengine.io.GameORM;
import com.example.gameengine.io.GameORMStandard;

/**
 * Created by rahul on 23/2/16.
 */
public class GameFactory {
    public static Game getGame(String name) {
        Game game = null;
        if ("LuckyNumber".equals(name)) {
            game = new LuckyNumberGame();
        }

        return game;
    }

    public static Game getGame(Integer gameId) {
        Game game = null;
        GameORM gameORM = new GameORMStandard(gameId);
        GameBean gameobj = gameORM.get();
        if ("LuckyNumber".equals(gameobj.getGameName())) {
            game = new LuckyNumberGame(gameORM);
        }

        return game;
    }
}

package com.example.gameengine.game.observers;

import com.example.gameengine.event.EventBean;
import com.example.gameengine.game.Game;
import com.example.gameengine.ws.Router;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class NotifyUsersObserver implements Observer {
    protected static final ObjectMapper mapper = new ObjectMapper();

    public void update(Observable obs, Object arg) {
        if (! (obs instanceof Game)) {
            return;
        }
        Game game = (Game) obs;
        Map<String, Object> params = new HashMap<>();
        params.put("id", game.getGameId());
        params.put("groupId", game.getGroupId());
        params.put("nextPlayer", game.getState().get("nextPlayer"));
        params.put("version", game.getState().get("version"));
        params.put("isActive", game.getState().get("isActive"));
        try {
            String message = mapper.writeValueAsString(new EventBean("game", (String) arg, params));
            for (Integer userId : game.getUserIds()) {
                Router.sendMessageToUser(userId, message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package com.example.gameengine.event;

import com.example.gameengine.exception.InvalidGameMove;
import com.example.gameengine.game.Game;
import com.example.gameengine.game.GameFactory;
import com.example.gameengine.game.observers.NotifyUsersObserver;
import com.example.gameengine.game.observers.TimeoutObserver;
import com.example.gameengine.ws.APIv1Socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rahul on 24/2/16.
 */
public class GameSystemEventHandler extends APISocketEventHandler {
    public GameSystemEventHandler(APIv1Socket apiSocket) {
        super(apiSocket);
    }

    public void processRequest(EventBean eventobj) throws IOException {
        String system = eventobj.getSystem();
        String event = eventobj.getEvent();
        Map<String, Object> params = eventobj.getParams();
        if ("game".equals(system)) {
            if ("new".equals(event)) {
                newGame(params);
            } else if ("move".equals(event)) {
                moveGame(params);
            }
        } else {
            successor.processRequest(eventobj);
        }
    }

    private void newGame(Map<String, Object> params) throws IOException {
        Game game = GameFactory.getGame((String) params.get("name"));
        game.addObserver(new NotifyUsersObserver());
        game.addObserver(new TimeoutObserver());
        try {
            game.newGame(apiSocket.getUserId(), params);
        } catch(InvalidGameMove e) {
            Map<String, Object> errorParams = new HashMap<>();
            errorParams.put("message", e.toString());
            String message = mapper.writeValueAsString(
                    new EventBean("game", "error", errorParams));
            apiSocket.getSession().getBasicRemote().sendText(message);
        }
    }

    private void moveGame(Map<String, Object> params) throws IOException {
        Game game = GameFactory.getGame((Integer) params.get("id"));
        game.addObserver(new NotifyUsersObserver());
        game.addObserver(new TimeoutObserver());
        try {
            game.moveGame(apiSocket.getUserId(), params);
        } catch(InvalidGameMove e) {
            Map<String, Object> errorParams = new HashMap<>();
            errorParams.put("message", e.toString());
            String message = mapper.writeValueAsString(
                    new EventBean("game", "error", errorParams));
            apiSocket.getSession().getBasicRemote().sendText(message);
            return;
        }

    }
}

package com.example.gameengine.event;

import com.example.gameengine.ws.Router;

import java.io.IOException;
import java.util.Map;

/**
 * Created by rahul on 24/2/16.
 */
public class RouterSystemEventHandler extends RouterEventHandler {
    public RouterSystemEventHandler(Router router) {
        super(router);
    }

    public void processRequest(EventBean eventobj) throws IOException {
        String system = eventobj.getSystem();
        String event = eventobj.getEvent();
        Map<String, Object> params = eventobj.getParams();
        if ("router".equals(system) && "user".equals(event)) {
            router.sendMessageToUser((Integer) params.get("id"), (String) params.get("message"));
        } else {
            successor.processRequest(eventobj);
        }
    }
}

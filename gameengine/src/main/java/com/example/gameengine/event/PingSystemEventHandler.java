package com.example.gameengine.event;

import com.example.gameengine.ws.APIv1Socket;
import com.example.gameengine.ws.Router;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rahul on 24/2/16.
 */
public class PingSystemEventHandler extends APISocketEventHandler {
    public PingSystemEventHandler(APIv1Socket apiSocket) {
        super(apiSocket);
    }

    public void processRequest(EventBean eventobj) throws IOException {
        String system = eventobj.getSystem();
        String event = eventobj.getEvent();
        Map<String, Object> params = eventobj.getParams();
        if ("ping".equals(system) && "user".equals(event)) {
            Map<String, Object> pingParams = new HashMap<>();
            pingParams.put("from", apiSocket.getUserId());
            pingParams.put("message", params.get("message"));
            Router.sendMessageToUser((Integer) params.get("to"),
                    mapper.writeValueAsString(new EventBean("ping", "user", pingParams)));
        } else {
            successor.processRequest(eventobj);
        }
    }

}

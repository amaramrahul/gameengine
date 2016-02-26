package com.example.gameengine.event;

import com.example.gameengine.common.Utils;
import com.example.gameengine.ws.APIv1Socket;
import com.example.gameengine.ws.Router;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rahul on 24/2/16.
 */
public class AuthenticateSystemEventHandler extends APISocketEventHandler {
    private static final String USERAPI_ENDPOINT = "http://127.0.0.1:8080/";

    public AuthenticateSystemEventHandler(APIv1Socket apiSocket) {
        super(apiSocket);
    }

    public void processRequest(EventBean eventobj) throws IOException {
        String system = eventobj.getSystem();
        String event = eventobj.getEvent();
        Map<String, Object> params = eventobj.getParams();
        if ("authenticate".equals(system)) {
            if ("authRequest".equals(event)) {
                authRequest(params);
            }
        } else {
            if (apiSocket.getUserId() == null) {
                Map<String, Object> errorParams = new HashMap<>();
                errorParams.put("message", "Unauthorized");
                String message = mapper.writeValueAsString(
                        new EventBean("authenticate", "error", errorParams));
                apiSocket.getSession().getBasicRemote().sendText(message);
            } else {
                successor.processRequest(eventobj);
            }
        }
    }

    private void authRequest(Map<String, Object> params) throws IOException {
        String response = Utils.sendHttpPostRequest(
                new URL(new URL(USERAPI_ENDPOINT), "/user/authenticate/"),
                mapper.writeValueAsString(params));
        Integer userId = (Integer) ((Map<String, Object>) mapper.readValue(response, Map.class)).get("id");
        if (userId != null) {
            apiSocket.userAuthenticated(userId);
        }
        Map<String, Object> authResponseParams = new HashMap<>();
        authResponseParams.put("id", userId);
        String message = mapper.writeValueAsString(
                new EventBean("authenticate", "authResponse", authResponseParams));
        apiSocket.getSession().getBasicRemote().sendText(message);
    }
}

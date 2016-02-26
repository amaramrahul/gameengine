package com.example.gameengine.ws;

import com.example.gameengine.event.EventBean;
import com.example.gameengine.event.RouterSystemEventHandler;
import com.example.gameengine.io.ClientAddressORM;
import com.example.gameengine.io.ClientAddressStandard;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rahul on 22/2/16.
 */
@ClientEndpoint
@ServerEndpoint("/router/")
public class Router {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static ConcurrentHashMap<Integer, Session> registeredUserSessions = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Session> routes = new ConcurrentHashMap<>();
    private final RouterSystemEventHandler routerSystemEventHandler;

    public Router() {
        routerSystemEventHandler = new RouterSystemEventHandler(this);
    }

    public static void registerUser(Integer userId, Session session) {
        registeredUserSessions.put(userId, session);
        ClientAddressORM clientAddressORM = new ClientAddressStandard(userId);
        clientAddressORM.set(session.getUserProperties().get("javax.websocket.endpoint.localAddress").toString().substring(1));
    }

    public static void deregisterUser(Integer userId) {
        registeredUserSessions.remove(userId);
        ClientAddressORM clientAddressORM = new ClientAddressStandard(userId);
        clientAddressORM.remove();
    }

    public static void sendMessageToUser(Integer userId, String message) throws IOException {
        if (registeredUserSessions.containsKey(userId)) {
            System.out.println("Sending message: " + message + " to userId: " + userId);
            registeredUserSessions.get(userId).getBasicRemote().sendText(message);
        } else {
            System.out.println("Routing message: " + message + " to userId: " + userId);
            // Get destination to route to for forwarding that message
            ClientAddressORM clientAddressORM = new ClientAddressStandard(userId);
            String destination = clientAddressORM.get();
            // Open websocket connection if not already done so to that destination
            // we use double check synchronization to ensure that we only open a socket only if necessary
            if (!routes.containsKey(destination)) {
                synchronized (routes) {
                    if (!routes.containsKey(destination)) {
                        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                        Router socket = new Router();
                        URI uri = null;
                        try {
                            uri = new URI("ws://" + destination + "/router/");
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        try {
                            Session session =  container.connectToServer(socket, uri);
                            session.setMaxIdleTimeout(0);
                            routes.put(destination,session);
                        } catch (DeploymentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            // Send message
            Map<String, Object> params = new HashMap<>();
            params.put("id", userId);
            params.put("message", message);
            routes.get(destination).getBasicRemote().sendText(
                    mapper.writeValueAsString(new EventBean("router", "user", params)));
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket opened: " + session.getId());
    }

    @OnMessage
    public void onMessage(String txt, Session session) throws IOException {
        System.out.println("Messsage recieved: " + txt);
        EventBean eventobj = mapper.readValue(txt, EventBean.class);
        routerSystemEventHandler.processRequest(eventobj);
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase());
    }

    @OnError
    public void onError(Throwable cause) {
        // Handle error
    }
}

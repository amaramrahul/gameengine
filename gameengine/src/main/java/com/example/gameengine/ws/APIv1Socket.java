package com.example.gameengine.ws;

import com.example.gameengine.event.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/api/v1/")
public class APIv1Socket {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final APISocketEventHandler authenticateSystemEventHandler;
    private final APISocketEventHandler pingSystemEventHandler;
    private final APISocketEventHandler gameSystemEventHandler;

    Integer userId = null;
    Session session = null;

    public APIv1Socket() {
        authenticateSystemEventHandler = new AuthenticateSystemEventHandler(this);
        pingSystemEventHandler = new PingSystemEventHandler(this);
        gameSystemEventHandler = new GameSystemEventHandler(this);

        authenticateSystemEventHandler.setSuccessor(pingSystemEventHandler);
        pingSystemEventHandler.setSuccessor(gameSystemEventHandler);
    }

    public Integer getUserId() {
        return userId;
    }

    public Session getSession() {
        return session;
    }

    public void userAuthenticated(Integer userId) {
        this.userId = userId;
        Router.registerUser(userId, session);
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket opened: " + session.getId());
        this.session = session;
    }

    @OnMessage
    public void onMessage(String txt, Session session) throws IOException {
        System.out.println("Got message from client: " + txt);
        EventBean eventobj = mapper.readValue(txt, EventBean.class);
        authenticateSystemEventHandler.processRequest(eventobj);
    }


    @OnClose
    public void onClose(CloseReason reason, Session session) {
        System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase());
        Router.deregisterUser(userId);
    }

    @OnError
    public void onError(Throwable cause) {
        // handle Error
    }

}

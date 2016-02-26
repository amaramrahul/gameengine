package com.example.client;

import java.io.BufferedReader;
import java.io.IOException;

import javax.websocket.*;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ClientEndpoint
public class App {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static Map<Integer, Integer> game_ids_version = new HashMap<>();
    private Session session;
    private String server = null;
    private String username = null;

    public App(String server, String username) {
        this.server = server;
        this.username = username;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println("Connected to server");
        this.session = session;
        Map<String, Object> authRequestParams = new HashMap<>();
        authRequestParams.put("username", username);
        authRequestParams.put("password", "xxxxxx");
        sendMessage(buildMessage("authenticate", "authRequest", authRequestParams));
    }

    @OnMessage
    public void onText(String message, Session session) throws IOException {
        System.out.println("Message received from server: " + message);
        Map<String, Object> eventobj = mapper.readValue(message, Map.class);
        if ("game".equals(eventobj.get("system"))) {
            Map<String, Object> params = (Map<String, Object>) eventobj.get("params");
            game_ids_version.put((Integer) params.get("id"), (Integer) params.get("version"));
        }
        System.out.print("Enter command:");
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) throws IOException, URISyntaxException, DeploymentException {
        System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase() + ". Retrying ...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // handle interruption
        }
        connectToServer();
    }

    @OnError
    public void onError(Throwable cause) throws IOException, URISyntaxException, DeploymentException {
        System.out.println("Error connecting to server due to " + cause.getMessage());
    }

    public void sendMessage(String str) throws IOException {
        System.out.println("Sending message to server: " + str);
        session.getBasicRemote().sendText(str);
    }

    public synchronized void connectToServer() throws IOException, URISyntaxException, DeploymentException {
        String dest = "ws://" + server + "/api/v1/";
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, new URI(dest));
    }

    public static String buildMessage(String system, String event, Map<String, Object> params) throws JsonProcessingException {
        Map<String, Object> messageobj = new HashMap<>();
        messageobj.put("system", system);
        messageobj.put("event", event);
        messageobj.put("params", params);
        return mapper.writeValueAsString(messageobj);
    }

    public static void main(String[] args) throws IOException, URISyntaxException, DeploymentException {
        App socket = new App(args[0], args[1]);
        socket.connectToServer();

        System.out.println("================= Possible commands ==================");
        System.out.println("        Start a new game: n <groupid>");
        System.out.println("             Make a move: m <gameid> <number>");
        System.out.println("             Ping a user: p <userid> <msg>");
        System.out.println("======================================================");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.print("Enter command:");
            String input = br.readLine();
            if (input.startsWith("n ")) {
                String[] parts = input.split("\\s+");
                Map<String, Object> newGameParams = new HashMap<>();
                newGameParams.put("name", "LuckyNumber");
                newGameParams.put("groupId", Integer.parseInt(parts[1]));
                String message = buildMessage("game", "new", newGameParams);
                socket.sendMessage(message);
            } else if (input.startsWith("m ")) {
                String[] parts = input.split("\\s+");
                Map<String, Object> moveGameParams = new HashMap<>();
                moveGameParams.put("id", Integer.parseInt(parts[1]));
                moveGameParams.put("action", Integer.parseInt(parts[2]));
                moveGameParams.put("version", game_ids_version.get(Integer.parseInt(parts[1])));
                String message = buildMessage("game", "move", moveGameParams);
                socket.sendMessage(message);
            } else if (input.startsWith("p ")) {
                String[] parts = input.split("\\s+", 3);
                Map<String, Object> routeMessageParams = new HashMap<>();
                routeMessageParams.put("to", Integer.parseInt(parts[1]));
                routeMessageParams.put("message", parts[2]);
                String message = buildMessage("ping", "user", routeMessageParams);
                socket.sendMessage(message);
            }
        }
    }
}


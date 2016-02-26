package com.example.gameengine.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.gameengine.ws.APIv1Socket;

import java.io.IOException;

/**
 * Created by rahul on 24/2/16.
 */
public abstract class APISocketEventHandler {
    protected static final ObjectMapper mapper = new ObjectMapper();
    protected APISocketEventHandler successor;
    protected APIv1Socket apiSocket;

    public APISocketEventHandler(APIv1Socket apiSocket) {
        this.apiSocket = apiSocket;
    }

    public void setSuccessor(APISocketEventHandler successor) {
        this.successor = successor;
    }

    abstract public void processRequest(EventBean eventobj) throws IOException;
}

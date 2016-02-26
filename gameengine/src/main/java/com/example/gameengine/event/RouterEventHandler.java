package com.example.gameengine.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.gameengine.ws.Router;

import java.io.IOException;

/**
 * Created by rahul on 24/2/16.
 */
public abstract class RouterEventHandler {
    protected static final ObjectMapper mapper = new ObjectMapper();
    protected RouterEventHandler successor;
    protected Router router;

    public RouterEventHandler(Router router) {
        this.router = router;
    }

    public void setSuccessor(RouterEventHandler successor) {
        this.successor = successor;
    }

    abstract public void processRequest(EventBean eventobj) throws IOException;
}

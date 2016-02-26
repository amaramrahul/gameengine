package com.example.userapi.ws;

import com.example.userapi.authenticator.Authenticator;
import com.example.userapi.authenticator.AuthenticateRequestBean;
import com.example.userapi.authenticator.AuthenticateResponseBean;
import org.glassfish.jersey.server.JSONP;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    @POST
    @JSONP
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/authenticate")
    public AuthenticateResponseBean authenticate(AuthenticateRequestBean authRequestParams) {
        System.out.println("Received auth request for " + authRequestParams.getUsername());
        Authenticator authenticator = new Authenticator(authRequestParams);
        AuthenticateResponseBean authResponse = authenticator.authenticate();
        System.out.println("Sending auth response for username " + authRequestParams.getUsername() +
                " identified as userId " + authResponse.getId());
        return authenticator.authenticate();
    }
}

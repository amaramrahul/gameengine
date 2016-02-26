package com.example.userapi.authenticator;

/**
 * Created by rahul on 21/2/16.
 */
public class Authenticator {
    AuthenticateRequestBean authRequest;
    public Authenticator(AuthenticateRequestBean authRequest) {
        this.authRequest = authRequest;
    }
    public AuthenticateResponseBean authenticate() {
        AuthenticateResponseBean authResponse = new AuthenticateResponseBean();
        if (authRequest.getUsername().equals("user1")) {
            authResponse.setId(1);
        } else if (authRequest.getUsername().equals("user2")) {
            authResponse.setId(2);
        } else if (authRequest.getUsername().equals("user3")) {
            authResponse.setId(3);
        } else if (authRequest.getUsername().equals("user4")) {
            authResponse.setId(4);
        } else if (authRequest.getUsername().equals("user5")) {
            authResponse.setId(5);
        }
        return authResponse;
    }
}

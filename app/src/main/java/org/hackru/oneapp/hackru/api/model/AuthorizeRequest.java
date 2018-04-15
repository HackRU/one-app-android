package org.hackru.oneapp.hackru.api.model;

/**
 * Created by Sean on 3/21/2018.
 */

public class AuthorizeRequest {
    final String email;
    final String password;

    public AuthorizeRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

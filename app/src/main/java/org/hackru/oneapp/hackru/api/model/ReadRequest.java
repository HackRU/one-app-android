package org.hackru.oneapp.hackru.api.model;

import com.google.gson.annotations.SerializedName;

public class ReadRequest {
    public ReadRequest(String email) {
        this.query = new Query(email);
    }

    public Query query;

    public class Query {
        public Query(String email) {
            this.email = email;
        }

        public String email;
    }

}

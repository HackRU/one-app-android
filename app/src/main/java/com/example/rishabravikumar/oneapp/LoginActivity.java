package com.example.rishabravikumar.oneapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    RequestQueue mRequestQueue;

    EditText emailEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Instantiate the request queue
        mRequestQueue = Volley.newRequestQueue(this);
        //Save the EditText instances
        emailEditText = (EditText)findViewById(R.id.editEmail);
        passwordEditText = (EditText)findViewById(R.id.editPassword);

        Button signInButton = (Button)findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEditText.getText().toString();
                String passwordCandidate = passwordEditText.getText().toString();
                final String hashedPassword = md5(passwordCandidate);

                String reqUrl = "www.google.com";
                StringRequest authenticationRequest = new StringRequest(Request.Method.POST,
                    reqUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //If the user is authenticated, send them to the main application
                            Intent mainActivityIntent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(mainActivityIntent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("password", hashedPassword);
                        return params;
                    }
                };

                mRequestQueue.add(authenticationRequest);
            }
        });
    }


    /**
     * Returns the MD5 hash of a plain-text password.
     * @param passwordCandidate The plain text password
     * @return MD5 hash of passwordCandidate
     */
    public static final String md5(final String passwordCandidate) {

        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(passwordCandidate.getBytes());
            //Get the bytes corresponding to the hash
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}

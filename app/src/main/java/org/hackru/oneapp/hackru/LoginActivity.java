package org.hackru.oneapp.hackru;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.hackru.oneapp.hackru.api.model.AuthorizeRequest;
import org.hackru.oneapp.hackru.api.model.Login;
import org.hackru.oneapp.hackru.api.service.HackRUService;
import org.hackru.oneapp.hackru.utils.SharedPreferencesUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        // Check for improper input
        if (!validate()) {
            _loginButton.setEnabled(true);
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_ProgressDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString(); // Needs to be declared final so that we can use it in the overrided onResponse method below
        String password = _passwordText.getText().toString();

        // Authentication logic
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://m7cwj1fy7c.execute-api.us-west-2.amazonaws.com/mlhtest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HackRUService hackRUService = retrofit.create(HackRUService.class);
        hackRUService.authorize(new AuthorizeRequest(email, password)).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Log.i(TAG, "Post submitted to API!");
                if(response.body().getStatusCode() == 200) {
                    String body = response.body().getBody();
                    String token = body.substring(body.indexOf("token")+9, body.indexOf(',')-1);
                    SharedPreferencesUtility.setAuthToken(LoginActivity.this, token);
                    SharedPreferencesUtility.setEmail(LoginActivity.this, email);
                    onLoginSuccess();
                } else {
                    onLoginFailed(true);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
                onLoginFailed(false);
                progressDialog.dismiss();
            }
        });


//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 1000);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent loginActivityIntent = new Intent(this, MainActivity.class);
        startActivity(loginActivityIntent);
        finish();
    }

    public void onLoginFailed(Boolean connectionSuccess) {
        if(connectionSuccess) {
            Toast.makeText(getBaseContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getBaseContext(), "Cannot reach server", Toast.LENGTH_LONG).show();
        }

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}

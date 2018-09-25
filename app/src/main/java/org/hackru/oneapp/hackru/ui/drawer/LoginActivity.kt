package org.hackru.oneapp.hackru.ui.drawer

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import org.hackru.oneapp.hackru.HackRUApp
import org.hackru.oneapp.hackru.R
import org.hackru.oneapp.hackru.Utils
import org.hackru.oneapp.hackru.api.models.AuthorizeModel
import org.hackru.oneapp.hackru.api.services.LcsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    val TAG = "LoginActivity"

    @Inject lateinit var lcsService: LcsService
    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        (application as HackRUApp).appComponent.inject(this)

        // Auto-fill the email of the last logged-in user
        val email = Utils.SharedPreferences.getEmail(this)
        input_email.setText(email, TextView.BufferType.EDITABLE)

        button_login.setOnClickListener { login() }
        button_create_account.setOnClickListener {
            // TODO: Open browser for creating an account
        }
    }

    // TODO: Make this gross code follow MVVM and app architecture guidelines
    fun login() {
        if(!inputIsValid()) return
        val alertDialog = AlertDialog.Builder(this)
                .setView(layoutInflater.inflate(R.layout.dialog_progress_circle, null))
                .setTitle("Authenticating...")
                .setCancelable(false)
                .create()
        alertDialog.show()

        val email = input_email.text.toString()
        val password = input_password.text.toString()
        val request = AuthorizeModel.Request(email, password)
        lcsService.authorize(request).enqueue(object : Callback<AuthorizeModel.Response> {
            override fun onResponse(call: Call<AuthorizeModel.Response>, response: Response<AuthorizeModel.Response>) {
                if(response.isSuccessful) {
                    val retrofitBody = response.body()
                    if(retrofitBody?.statusCode == 200) {
                        val body: String = retrofitBody.body
                        val auth: AuthorizeModel.Auth = gson.fromJson<AuthorizeModel.Body>(body, AuthorizeModel.Body::class.java).auth
                        Utils.SharedPreferences.setEmail(this@LoginActivity, email)
                        Utils.SharedPreferences.setAuthToken(this@LoginActivity, auth.token)
                        // TODO: Implement canscan
                        alertDialog.dismiss()
                        finish()
                    } else {
                        val errorMessage = response.body()?.body ?: "Network error. Please try again"
                        showToast(errorMessage, Toast.LENGTH_SHORT)
                        alertDialog.dismiss()
                    }
                } else {
                    showToast("Network error. Please try again", Toast.LENGTH_SHORT)
                    alertDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<AuthorizeModel.Response>, t: Throwable) {
                if(t is IOException) {
                    showToast("Network error. Please try again", Toast.LENGTH_SHORT)
                    alertDialog.dismiss()
                } else {
                    showToast("Serialization error. Please report this error to rnd@hackru.org", Toast.LENGTH_LONG)
                    alertDialog.dismiss()
                }
            }
        })
    }

    fun showToast(message: String, duration: Int) {
        Toast.makeText(this, message, duration).show()
    }

    /**
     * Makes sure the provided email is not blank and is in email form. Also makes rue that the
     * provided password is not blank. If either the email or password do not meet these conditions,
     * then return false and show a warning dialog to the user.
     */
    fun inputIsValid(): Boolean {
        val email = input_email.text.toString()
        val password = input_password.text.toString()

        if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.error = "Please enter a valid email address"
            return false
        } else {
            input_email.error = null
        }

        if(password.isEmpty()) {
            input_password.error = "Please enter a password"
            return false
        } else {
            input_password.error = null
        }

        return true
    }
}

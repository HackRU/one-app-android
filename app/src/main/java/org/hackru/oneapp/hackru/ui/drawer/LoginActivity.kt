package org.hackru.oneapp.hackru.ui.drawer

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import org.hackru.oneapp.hackru.HackRUApp
import org.hackru.oneapp.hackru.R
import org.hackru.oneapp.hackru.Utils
import org.hackru.oneapp.hackru.api.models.AuthorizeModel
import org.hackru.oneapp.hackru.api.models.ReadModel
import org.hackru.oneapp.hackru.api.services.LcsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    val TAG = "LoginActivity"

    @Inject lateinit var lcsService: LcsService
    val gson = Gson()
    lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        (application as HackRUApp).appComponent.inject(this)

        // Auto-fill the email of the last logged-in user
        val email = Utils.SharedPreferences.getEmail(this)
        input_email.setText(email, TextView.BufferType.EDITABLE)

        loadingDialog = AlertDialog.Builder(this)
                .setView(layoutInflater.inflate(R.layout.dialog_progress_circle, null))
                .setTitle("Authenticating...")
                .setCancelable(false)
                .create()

        button_login.setOnClickListener { login() }
    }

    // TODO: Make this gross code follow MVVM and app architecture guidelines
    fun login() {
        if(!inputIsValid()) return
        loadingDialog.show()
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
                        val logoutAt: Long = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
                                .parse(auth.validUntil)
                                .time
                        Utils.SharedPreferences.setLogoutAt(this@LoginActivity, logoutAt)
                        retrieveNameAndCanScan(email, auth.token)
                    } else {
                        val errorMessage = response.body()?.body ?: "Network error. Please try again"
                        showToast(errorMessage, Toast.LENGTH_SHORT)
                        loadingDialog.dismiss()
                    }
                } else {
                    showToast("Network error. Please try again", Toast.LENGTH_SHORT)
                    loadingDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<AuthorizeModel.Response>, t: Throwable) {
                if(t is IOException) {
                    showToast("Network error. Please try again", Toast.LENGTH_SHORT)
                    loadingDialog.dismiss()
                } else {
                    showToast("Serialization error. Please report this error to rnd@hackru.org", Toast.LENGTH_LONG)
                    loadingDialog.dismiss()
                }
            }
        })
    }

    fun retrieveNameAndCanScan(email: String, authToken: String) {
        val request = ReadModel.Request(email, authToken, ReadModel.Query(email))
        lcsService.read(request).enqueue(object : Callback<ReadModel.Response> {
            override fun onResponse(call: Call<ReadModel.Response>, response: Response<ReadModel.Response>) {
                if(response.isSuccessful) {
                    val retrofitBody = response.body()
                    if(retrofitBody?.statusCode == 200) {
                        val role: ReadModel.Role = retrofitBody.body[0].role
                        Utils.SharedPreferences.setCanScan(this@LoginActivity, role.director || role.organizer)
                        val firstName: String = retrofitBody.body[0].firstName
                        val lastName: String = retrofitBody.body[0].lastName
                        var fullName: String = "$firstName $lastName"
                        if(firstName.isEmpty() || lastName.isEmpty()) {
                            fullName = ""
                        }
                        Utils.SharedPreferences.setName(this@LoginActivity, fullName)
                        loadingDialog.dismiss()
                        finish()
                    } else {
                        showToast("Error reading user data. Please try again", Toast.LENGTH_SHORT)
                        loadingDialog.dismiss()
                    }
                } else {
                    showToast("Network error. Please try again", Toast.LENGTH_SHORT)
                    loadingDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<ReadModel.Response>, t: Throwable) {
                if(t is IOException) {
                    showToast("Network error. Please try again", Toast.LENGTH_SHORT)
                    loadingDialog.dismiss()
                } else {
                    showToast("Serialization error. Please report this error to rnd@hackru.org", Toast.LENGTH_LONG)
                    loadingDialog.dismiss()
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

package org.hackru.oneapp.hackru.ui.drawer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_login.*
import org.hackru.oneapp.hackru.R
import org.hackru.oneapp.hackru.Utils

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // TODO: Implement login

        // Auto-fill the email of the last logged-in user
        val email = Utils.SharedPreferences.getEmail(this)
        input_email.setText(email, TextView.BufferType.EDITABLE)


    }
}

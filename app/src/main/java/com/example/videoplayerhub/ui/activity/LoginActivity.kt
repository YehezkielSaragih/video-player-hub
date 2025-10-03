package com.example.videoplayerhub.ui.activity

import android.content.Intent
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.example.videoplayerhub.R
import com.example.videoplayerhub.config.Prefs
import com.example.videoplayerhub.viewmodel.LoginViewModel
import androidx.lifecycle.Observer

class LoginActivity : ComponentActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        // Observer login state
        loginViewModel.loginState.observe(this, Observer { result ->
            result.onSuccess { token ->
                Prefs.saveToken(this, token)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            result.onFailure {
                Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show()
            }
        })

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.login_credentials_required, Toast.LENGTH_SHORT).show()
            } else {
                loginViewModel.login(email, password)
            }
        }
    }
}
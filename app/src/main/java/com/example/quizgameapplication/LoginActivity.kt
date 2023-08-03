package com.example.quizgameapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizgameapplication.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root

        setContentView(view)

        loginBinding.buttonLoginSignIn.setOnClickListener {

        }

        loginBinding.buttonLoginGoogleSignIn.setOnClickListener {

        }

        loginBinding.textViewLoginSignUp.setOnClickListener {

        }

        loginBinding.textViewLoginForgotPassword.setOnClickListener {

        }
    }
}
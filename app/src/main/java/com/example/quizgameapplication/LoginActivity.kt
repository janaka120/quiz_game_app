package com.example.quizgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.quizgameapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding

    val authFirebase = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root

        setContentView(view)

        loginBinding.buttonLoginSignIn.setOnClickListener {
            val email = loginBinding.editTextLoginEmail.text.toString()
            val password = loginBinding.editTextLoginPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginWithEmailAndPassword(email, password)
            }else {
                Toast.makeText(applicationContext, "Please enter value for email & password", Toast.LENGTH_SHORT).show()
            }
        }

        loginBinding.buttonLoginGoogleSignIn.setOnClickListener {
        }

        loginBinding.textViewLoginSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        loginBinding.textViewLoginForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

    }
        fun loginWithEmailAndPassword(email: String, password: String) {
            loginBinding.progressBarLogin.visibility = View.VISIBLE
            loginBinding.buttonLoginSignIn.isClickable = false
            authFirebase.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "Welcome to Quiz app", Toast.LENGTH_SHORT).show()
                    finish()
                }else {
                    Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    println("User login error >>>" + task.exception?.localizedMessage)
                }

                loginBinding.progressBarLogin.visibility = View.INVISIBLE
                loginBinding.buttonLoginSignIn.isClickable = true
            }
        }

    override fun onStart() {
        super.onStart()

        val user = authFirebase.currentUser
        if (user != null) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(applicationContext, "Welcome to Quiz app", Toast.LENGTH_SHORT).show()
        }
    }
}

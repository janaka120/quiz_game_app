package com.example.quizgameapplication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.quizgameapplication.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding

    private val authFirebase = FirebaseAuth.getInstance()
    lateinit var googleSignInClient: GoogleSignInClient

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root

        setContentView(view)

        val textOfGoogleButton = loginBinding.buttonLoginGoogleSignIn.getChildAt(0) as TextView
        textOfGoogleButton.text = "Continue with Google"
        textOfGoogleButton.setTextColor(Color.BLACK)
        textOfGoogleButton.textSize = 18F

        // register
        registerActivityForGoogleSignIn()

        loginBinding.buttonLoginSignIn.setOnClickListener {
            val email = loginBinding.editTextLoginEmail.text.toString()
            val password = loginBinding.editTextLoginPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginWithEmailAndPassword(email, password)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please enter value for email & password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        loginBinding.buttonLoginGoogleSignIn.setOnClickListener {
            signInGoogle()
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

    private fun loginWithEmailAndPassword(email: String, password: String) {
        loginBinding.progressBarLogin.visibility = View.VISIBLE
        loginBinding.buttonLoginSignIn.isClickable = false
        authFirebase.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(applicationContext, "Welcome to Quiz app", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(
                    applicationContext,
                    task.exception?.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
                println("User login error >>>" + task.exception?.localizedMessage)
            }

            loginBinding.progressBarLogin.visibility = View.INVISIBLE
            loginBinding.buttonLoginSignIn.isClickable = true
        }
    }

    private fun signInGoogle() {
        loginBinding.progressBarLogin.visibility = View.VISIBLE
        loginBinding.buttonLoginGoogleSignIn.isClickable = false
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1023444390308-bisrbc7t88uvlfr85o3mt3rtr1krfed6.apps.googleusercontent.com")
            .requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        signIn()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }

    private fun registerActivityForGoogleSignIn() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == RESULT_OK && data != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(data)
                    firebaseSignInWithGoogle(task)
                }else {
                    loginBinding.progressBarLogin.visibility = View.VISIBLE
                    loginBinding.buttonLoginGoogleSignIn.isClickable = false
                    Toast.makeText(
                        applicationContext,
                        "Fail to Google signIn",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun firebaseSignInWithGoogle(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext, "Welcome to Quiz Game", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)

            loginBinding.progressBarLogin.visibility = View.INVISIBLE
            loginBinding.buttonLoginGoogleSignIn.isClickable = true

            finish()
            firebaseGoogleAccount(account)
        } catch (e: ApiException) {
            Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }

    }
    private fun firebaseGoogleAccount(account: GoogleSignInAccount) {
        val authCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        authFirebase.signInWithCredential(authCredential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = authFirebase.currentUser
            }else {
                Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
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

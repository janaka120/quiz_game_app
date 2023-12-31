package com.example.quizgameapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.quizgameapplication.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    lateinit var signupBinding: ActivitySignupBinding

    private var auth: FirebaseAuth = FirebaseAuth.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        val view = signupBinding.root
        setContentView(view)

        signupBinding.buttonSignup.setOnClickListener {
            val email = signupBinding.editTextSignupEmail.text.toString()
            val password = signupBinding.editTextSignupPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                signupWithFirebase(email, password)
            }else {
                Toast.makeText(applicationContext, "Please enter values for email & password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signupWithFirebase(email: String, password: String) {
        signupBinding.progressBarSignup.visibility = View.VISIBLE
        signupBinding.buttonSignup.isClickable = false

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful) {

                Toast.makeText(applicationContext, "Account has been created", Toast.LENGTH_SHORT).show()
                finish()


            }else {
                Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                println("User create error >>>" + task.exception?.localizedMessage)
            }

            signupBinding.progressBarSignup.visibility = View.INVISIBLE
            signupBinding.buttonSignup.isClickable = true
        }
    }
}
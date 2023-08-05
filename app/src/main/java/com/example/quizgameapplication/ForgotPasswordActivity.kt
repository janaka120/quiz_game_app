package com.example.quizgameapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quizgameapplication.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var bindingForgot: ActivityForgotPasswordBinding

    val authFirebase = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingForgot = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = bindingForgot.root
        setContentView(view)

        bindingForgot.buttonResetPassword.setOnClickListener {
            val email = bindingForgot.editTextForgotEmail.text.toString()
            if (email.isNotEmpty()) {
                sendEmail(email)
            }else {
                Toast.makeText(applicationContext, "Please enter values for email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun sendEmail(email: String) {
        authFirebase.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                Toast.makeText(applicationContext, "Send mail successfully", Toast.LENGTH_SHORT).show()
                finish()
            }else {
                Toast.makeText(applicationContext, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
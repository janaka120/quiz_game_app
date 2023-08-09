package com.example.quizgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizgameapplication.databinding.ActivityResultBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ResultActivity : AppCompatActivity() {
    lateinit var activityResultBinding: ActivityResultBinding

    private val firebaseDb: FirebaseDatabase = FirebaseDatabase.getInstance()
    val databaseRef = firebaseDb.reference.child("scores")

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser

    var userCorrect = ""
    var userWrong = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultBinding = ActivityResultBinding.inflate(layoutInflater)
        val view = activityResultBinding.root

        setContentView(view)

        activityResultBinding.buttonResultExit.setOnClickListener {
            finishAffinity();
        }

        activityResultBinding.buttonResultPlayAgain.setOnClickListener {
            val intent = Intent(this@ResultActivity, MainActivity::class.java)
            startActivity(intent)

            finish()
            System.exit(0)
        }

        databaseRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser?.let {
                    val userUid = it.uid
                    userCorrect  = snapshot.child(userUid).child("correct").value.toString()
                    userWrong  = snapshot.child(userUid).child("wrong").value.toString()

                    activityResultBinding.textViewResultCorrect.text = userCorrect
                    activityResultBinding.textViewResultWrong.text = userWrong
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}
package com.example.quizgameapplication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.quizgameapplication.databinding.ActivityQuizBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuizActivity : AppCompatActivity() {
    lateinit var quizBinding: ActivityQuizBinding

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val dataBaseRef = firebaseDatabase.reference.child("questions")

    var question = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""
    var correctAnswer = ""
    var questionCount = 0
    var questionNumber = 1

    private var userAnswer = ""
    var userCorrectAnswerCount = 0
    var userWrongAnswerCount = 0

    lateinit var timer: CountDownTimer
    private val totalTime = 25000L // ms
    var timerContinue = false
    var leftTime = totalTime

    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser

    private val scoreRef = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = ActivityQuizBinding.inflate(layoutInflater)
        val view = quizBinding.root
        setContentView(view)

        gameLogic()

        quizBinding.buttonQuizNext.setOnClickListener {
            restoreOptions()
            resetTimer()
            gameLogic()
        }

        quizBinding.buttonQuizFinish.setOnClickListener {
            sendScore()
        }

        quizBinding.textViewA.setOnClickListener {
            userAnswer = "a"

            pauseTimer()

            if (userAnswer == correctAnswer) {
                quizBinding.textViewA.setBackgroundColor(Color.GREEN)
                userCorrectAnswerCount ++
                quizBinding.textViewQuizCorrectAnswer.text = userCorrectAnswerCount.toString()
            }else {
                quizBinding.textViewA.setBackgroundColor(Color.RED)
                userWrongAnswerCount ++
                quizBinding.textViewQuizWrongAnswer.text = userWrongAnswerCount.toString()

                findAnswer()
            }
            disabledClickableOfOptions()
        }
        quizBinding.textViewB.setOnClickListener {
            userAnswer = "b"

            pauseTimer()

            if (userAnswer == correctAnswer) {
                quizBinding.textViewB.setBackgroundColor(Color.GREEN)
                userCorrectAnswerCount ++
                quizBinding.textViewQuizCorrectAnswer.text = userCorrectAnswerCount.toString()
            }else {
                quizBinding.textViewB.setBackgroundColor(Color.RED)
                userWrongAnswerCount ++
                quizBinding.textViewQuizWrongAnswer.text = userWrongAnswerCount.toString()

                findAnswer()
            }

            disabledClickableOfOptions()
        }
        quizBinding.textViewC.setOnClickListener {
            userAnswer = "c"

            pauseTimer()

            if (userAnswer == correctAnswer) {
                quizBinding.textViewC.setBackgroundColor(Color.GREEN)
                userCorrectAnswerCount ++
                quizBinding.textViewQuizCorrectAnswer.text = userCorrectAnswerCount.toString()
            }else {
                quizBinding.textViewC.setBackgroundColor(Color.RED)
                userWrongAnswerCount ++
                quizBinding.textViewQuizWrongAnswer.text = userWrongAnswerCount.toString()

                findAnswer()
            }

            disabledClickableOfOptions()
        }
        quizBinding.textViewD.setOnClickListener {
            userAnswer = "d"

            pauseTimer()

            if (userAnswer == correctAnswer) {
                quizBinding.textViewD.setBackgroundColor(Color.GREEN)
                userCorrectAnswerCount ++
                quizBinding.textViewQuizCorrectAnswer.text = userCorrectAnswerCount.toString()
            }else {
                quizBinding.textViewD.setBackgroundColor(Color.RED)
                userWrongAnswerCount ++
                quizBinding.textViewQuizWrongAnswer.text = userWrongAnswerCount.toString()

                findAnswer()
            }

            disabledClickableOfOptions()
        }
    }

    private fun findAnswer() {
        when(correctAnswer) {
            "a" -> quizBinding.textViewA.setBackgroundColor(Color.GREEN)
            "b" -> quizBinding.textViewB.setBackgroundColor(Color.GREEN)
            "c" -> quizBinding.textViewC.setBackgroundColor(Color.GREEN)
            "d" -> quizBinding.textViewD.setBackgroundColor(Color.GREEN)
        }
    }

    private fun disabledClickableOfOptions() {
        quizBinding.textViewA.isClickable = false
        quizBinding.textViewB.isClickable = false
        quizBinding.textViewC.isClickable = false
        quizBinding.textViewD.isClickable = false
    }


    private fun gameLogic(){

        dataBaseRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                questionCount = snapshot.childrenCount.toInt()
                if (questionNumber <= questionCount) {
                    question = snapshot.child(questionNumber.toString()).child("q").value.toString()
                    answerA = snapshot.child(questionNumber.toString()).child("a").value.toString()
                    answerB = snapshot.child(questionNumber.toString()).child("b").value.toString()
                    answerC = snapshot.child(questionNumber.toString()).child("c").value.toString()
                    answerD = snapshot.child(questionNumber.toString()).child("d").value.toString()
                    correctAnswer = snapshot.child(questionNumber.toString()).child("answer").value.toString()

                    quizBinding.textViewA.text = answerA
                    quizBinding.textViewB.text = answerB
                    quizBinding.textViewC.text = answerC
                    quizBinding.textViewD.text = answerD
                    quizBinding.textViewQ.text = question

                    quizBinding.progressBarQuiz.visibility = View.INVISIBLE
                    quizBinding.linearLayoutInfo.visibility = View.VISIBLE
                    quizBinding.linearLayoutQuestion.visibility = View.VISIBLE
                    quizBinding.linearLayoutButtons.visibility = View.VISIBLE

                    startTimer()

                }else {
                    val dialogMessage = AlertDialog.Builder(this@QuizActivity)
                    dialogMessage.setTitle("Quiz Game")
                    dialogMessage.setMessage("Congratulations!\n You have answer all the questions. Do you want to see the results?")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See Results"){dialogWindow, i ->
                        sendScore()
                    }
                    dialogMessage.setNegativeButton("Play again"){dialogWindow, i ->
                        val intent = Intent(this@QuizActivity, MainActivity::class.java)
                        startActivity(intent)

                        finish()
                    }

                    dialogMessage.create().show()
                }

                questionNumber ++
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun restoreOptions() {
        quizBinding.textViewA.setBackgroundColor(Color.WHITE)
        quizBinding.textViewB.setBackgroundColor(Color.WHITE)
        quizBinding.textViewC.setBackgroundColor(Color.WHITE)
        quizBinding.textViewD.setBackgroundColor(Color.WHITE)

        quizBinding.textViewA.isClickable = true
        quizBinding.textViewB.isClickable = true
        quizBinding.textViewC.isClickable = true
        quizBinding.textViewD.isClickable = true
    }

    private fun startTimer() {
        timer = object : CountDownTimer(leftTime, 1000){
            override fun onTick(millisUntilFinish: Long) {
                leftTime = millisUntilFinish
                updateCountDownText()
            }

            override fun onFinish() {
                disabledClickableOfOptions()
                resetTimer()
                updateCountDownText()
                quizBinding.textViewQ.text = "Sorry, Time is up! Continue with next question"
                timerContinue = false
            }
        }.start()

        timerContinue = true
    }

    private fun updateCountDownText() {
        val remainingTime: Int = (leftTime/1000).toInt()
        quizBinding.textViewQuizTime.text = remainingTime.toString()
    }

    private fun pauseTimer() {
        timer.cancel()
        timerContinue = false
    }

    private fun resetTimer() {
        pauseTimer()
        leftTime = totalTime
        updateCountDownText()
    }

    private fun sendScore() {
        user?.let {
            val userUID = it.uid
            scoreRef.child("scores").child(userUID).child("correct").setValue(userCorrectAnswerCount)
            scoreRef.child("scores").child(userUID).child("wrong").setValue(userWrongAnswerCount).addOnSuccessListener {
                Toast.makeText(applicationContext, "Scores sent to database successfully", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@QuizActivity, ResultActivity::class.java)
                startActivity(intent)

                finish()
            }
        }
    }
}
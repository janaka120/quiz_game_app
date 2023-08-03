package com.example.quizgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.example.quizgameapplication.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    lateinit var activityWelcomeBinding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWelcomeBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        var view = activityWelcomeBinding.root
        setContentView(view)

        val alphaAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.splash_anim)
        activityWelcomeBinding.textView.startAnimation(alphaAnimation)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed(object: Runnable {
            override fun run() {
                val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 4000)
    }
}
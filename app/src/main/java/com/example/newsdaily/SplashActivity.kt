package com.example.newsdaily

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val splashTimeout : Long = 1857
    private lateinit var introManager: IntroManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        introManager = IntroManager(this)

        Handler(Looper.getMainLooper()).postDelayed({

            if(introManager.check())
            {
                introManager.setFirst(false)
                val intent = Intent(this,IntroActivity::class.java)
                startActivity(intent)
                finish()

            }
            else
            {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, splashTimeout)
    }

}
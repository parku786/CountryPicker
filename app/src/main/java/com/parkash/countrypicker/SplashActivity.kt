package com.parkash.countrypicker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        this.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }, 1500)
    }
}

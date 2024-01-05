package com.example.proyecto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val logo = findViewById<CircleImageView>(R.id.logo)
        val animation = AnimationUtils.loadAnimation(this, R.anim.rotate_img)
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)
        bounce.interpolator = BounceInterpolator()

        val set = AnimationSet(false)
        set.addAnimation(animation)
        set.addAnimation(bounce)
        logo.startAnimation(set)

        Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            overridePendingTransition(R.anim.slide_up, R.anim.no_animation)

        }, 3000) // Muestra la pantalla de bienvenida durante 3 segundos
    }

}
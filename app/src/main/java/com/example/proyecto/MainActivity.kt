package com.example.proyecto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.LoginActivity.Companion.useremail
import com.example.proyecto.adapter.AlmacenAdapter
import com.example.proyecto.fragments.MainFragment
import com.example.proyecto.fragments.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private var backPressedTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView =
            findViewById<View>(R.id.bottomNavigationView) as BottomNavigationView

        var mainFragment = MainFragment()
        var userFragment = UserFragment()



        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, mainFragment)
            commit()
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    mainFragment = MainFragment()
                    setCurrentFragment(mainFragment)
                }

                R.id.nav_user -> {
                    userFragment = UserFragment()
                    setCurrentFragment(userFragment)
                }

                R.id.nav_exit -> {
                    signOut()
                }
            }
            true


        }


    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }

    private fun signOut(){
        useremail = ""
        MainFragment.editAlmacen = Almacen()
        Firebase.auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
    }


    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        if (fragment is BackPressedListener) {
            (fragment as BackPressedListener).onBackPressed()
        } else {
            super.onBackPressed()
        }
    }


    interface BackPressedListener {
        fun onBackPressed()
    }




}
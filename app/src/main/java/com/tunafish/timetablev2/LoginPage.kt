package com.tunafish.timetablev2


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.google.android.material.floatingactionbutton.FloatingActionButton


class LoginPage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val navigateToLogin: FloatingActionButton = findViewById(R.id.back)
        navigateToLogin.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            //finish()
        }
    }
}

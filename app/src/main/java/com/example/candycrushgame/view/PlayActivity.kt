package com.example.candycrushgame.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.candycrushgame.MainActivity
import com.example.candycrushgame.R
// PlayActivity class responsible for displaying the play screen
class PlayActivity : AppCompatActivity() {
    private lateinit var playBtn :ImageView
    // Method called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for the activity
        setContentView(R.layout.activity_play)
        // Hide the action bar
        val action =  supportActionBar
        action?.hide()
        // Initialize playBtn and set click listener to start MainActivity
        playBtn = findViewById(R.id.playBtn)
        playBtn.setOnClickListener {
            startActivity(Intent(this@PlayActivity,
                MainActivity::class.java))
        }  // Start MainActivity when playBtn is clicked
    }
}
package com.example.candycrushgame.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.candycrushgame.R
import kotlinx.coroutines.delay


// WelcomeActivity class responsible for displaying the welcome screen
class WelcomeActivity : AppCompatActivity() {
    // Method called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for the activity
        setContentView(R.layout.activity_welcome)
        // Hide the action bar

        val action =  supportActionBar
        action?.hide()
        try{
            // Use Handler to delay the execution of code for 3 seconds
            Handler().postDelayed({
                // Start PlayActivity after 3 seconds
                startActivity(Intent
                    (this@WelcomeActivity,
                    PlayActivity::class.java))
            },3000)
        }
        catch (e:Exception){
            // Handle any exceptions that may occur during the delay
            e.printStackTrace()
        }

    }
}
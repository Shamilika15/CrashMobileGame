package com.example.candycrushgame

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.candycrushgame.uiltel.OnSwipeListener
import java.util.Arrays.asList

class MainActivity : AppCompatActivity() {

    // Array of candy drawables
    var candies = intArrayOf(
        R.drawable.bluecandy,
        R.drawable.greencandy,
        R.drawable.orangecandy,
        R.drawable.purplecandy,
        R.drawable.redcandy,
        R.drawable.yellowcandy,

    )
    // Width of each candy block
    var widthOfBlock :Int = 0
    var noOfBlock : Int = 8
    var widthOfScreen :Int = 0
    lateinit var candy :ArrayList<ImageView>
    var candyToBeDragged : Int = 0
    var candyToBeReplaced : Int = 0
    var notCandy : Int = R.drawable.transparent

    lateinit var mHandler :Handler // Handler for managing delayed tasks
    private lateinit var scoreResult :TextView // TextView for displaying the score
    var score = 0 // Variable to hold the current score
    var interval = 100L // Interval for repeating actions
    private var highScore = 0 // Variable to hold the high score
    private lateinit var wrongMovesTextView: TextView // TextView for displaying wrong move count
    private var wrongMoveCount = 0  // Variable to hold the count of wrong moves
    private val maxWrongMoves = 20 // Maximum allowed wrong moves

    private lateinit var sharedPreferences:SharedPreferences  // SharedPreferences for storing high score

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize views and SharedPreferences
         scoreResult = findViewById(R.id.score)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        highScore = sharedPreferences.getInt("highScore", 0)
        displayHighScore() // Display the high score
        wrongMovesTextView = findViewById(R.id.wrongMoves)
        // Get display metrics to calculate width of screen
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        // Calculate width of each candy block based on screen width
        widthOfScreen = displayMetrics.widthPixels
        // Set click listener for exit button
        var heightOfScreen = displayMetrics.heightPixels
        widthOfBlock = widthOfScreen / noOfBlock

        val exitButton = findViewById<Button>(R.id.exitButton)
        exitButton.setOnClickListener {
            showExitConfirmationDialog() // Show confirmation dialog before exiting the game
        }
        // Create the game board and set swipe listeners for candies
        candy = ArrayList()
        createBoard()
        for (imageView in candy){
            imageView.setOnTouchListener(
                object :OnSwipeListener(this){
                    override fun onSwipeRight() {
                        super.onSwipeRight()
                        candyToBeDragged = imageView.id
                        candyToBeReplaced = candyToBeDragged + 1
                        candyInterChacge() // Interchange candies on swipe right
                    }
                    override fun onSwipeLift() {
                        super.onSwipeLift()
                        candyToBeDragged = imageView.id
                        candyToBeReplaced = candyToBeDragged - 1
                        candyInterChacge() // Interchange candies on swipe left
                    }
                    override fun onSwipeTop() {
                        super.onSwipeTop()
                        candyToBeDragged = imageView.id
                        candyToBeReplaced = candyToBeDragged - noOfBlock
                        candyInterChacge() // Interchange candies on swipe top
                    }
                    override fun onSwipeBottom() {
                        super.onSwipeBottom()
                        candyToBeDragged = imageView.id
                        candyToBeReplaced = candyToBeDragged + noOfBlock
                        candyInterChacge() // Interchange candies on swipe bottom
                    }

            }
            )
        }



        mHandler = Handler()
        startRepeat() // Start repeating actions

    }
    private fun displayHighScore() {
        // Display high score in TextView
        val highScoreTextView = findViewById<TextView>(R.id.highscore)
        highScoreTextView.text = "High Score: $highScore"
    }

    private fun updateHighScore(score: Int) {
        if (score > highScore) {
            // Update high score
            highScore = score

            // Save high score to SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putInt("highScore", highScore)
            editor.apply()

            // Update UI to display new high score
            displayHighScore()
        }
    }


    private fun candyInterChacge() {
        // Retrieve the background resource of the candies to be swapped

        var background :Int = candy.get(candyToBeReplaced).tag as Int
        var background1 :Int = candy.get(candyToBeDragged).tag as Int
        // Swap the background resources between the candies
        candy.get(candyToBeDragged).setImageResource(background)
        candy.get(candyToBeReplaced).setImageResource(background1)
        // Swap the tags (background resources) between the candies
        candy.get(candyToBeDragged).setTag(background)
        candy.get(candyToBeReplaced).setTag(background1)
        // Check if the swapped candies have different backgrounds
        if (background != background1) {
            wrongMoveCount++
            updateWrongMoveCount()
            if (wrongMoveCount >= maxWrongMoves) {
                showMaxWrongMovesAlert()
            }
        }

    }
    // Function to check each row for three consecutive candies of the same type
    private fun checkRowForThree(){
        for (i in 0..61){
            var chosedCandy = candy.get(i).tag
            var isBlank :Boolean  = candy.get(i).tag == notCandy
            val notValid = arrayOf(6,7,14,15,22,23,30,31,38,39,46,47,54,55)
            val list = asList(*notValid)
            if(!list.contains(i)){
                var x = i

                if(candy.get(x++).tag as Int == chosedCandy
                    && !isBlank
                    && candy.get(x++).tag as Int == chosedCandy
                    && candy.get(x).tag as Int == chosedCandy
                    ){


                       score = score + 3
                    scoreResult.text = "$score"
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                    x--
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                    x--
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                }
            }
        }
        // Move down candies to fill any empty spaces after removing matched candies
        moveDownCandies()
    }
    private fun checkColumnForThree(){
        // Retrieve the background resource of the candies to be swapped
        updateHighScore(score)
        for (i in 0..47){
            // Swap the background resources between the candies
            var chosedCandy = candy.get(i).tag
            var isBlank :Boolean  = candy.get(i).tag == notCandy
            var x = i

                if(candy.get(x).tag as Int == chosedCandy
                    && !isBlank
                    && candy.get(x+noOfBlock).tag as Int == chosedCandy
                    && candy.get(x+2*noOfBlock).tag as Int == chosedCandy
                ){


                    score = score + 3
                    scoreResult.text = "$score"
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                    x = x + noOfBlock
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                    x = x + noOfBlock
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                }

        }
        moveDownCandies()
    }

    private fun moveDownCandies() {

        val firstRow = arrayOf(1,2,3,4,5,6,7,)
        val list = asList(*firstRow)
        for (i in 55 downTo 0){
            if(candy.get(i+noOfBlock).tag as Int == notCandy){

                candy.get(i+noOfBlock).setImageResource(candy.get(i).tag as Int)
                candy.get(i+noOfBlock).setTag(candy.get(i).tag as Int)

                candy.get(i).setImageResource(notCandy)
                candy.get(i).setTag(notCandy)
                if (list.contains(i) && candy.get(i).tag == notCandy){
                    var randomColor : Int = Math.abs(Math.random()* candies.size).toInt()
                    candy.get(i).setImageResource(candies[randomColor])
                    candy.get(i).setTag(candies[randomColor])
                }
            }
        }
        for(i in 0..7){
            if (candy.get(i).tag as Int == notCandy){

                var randomColor : Int = Math.abs(Math.random()* candies.size).toInt()
                candy.get(i).setImageResource(candies[randomColor])
                candy.get(i).setTag(candies[randomColor])
            }
        }

    }
     val repeatChecker :Runnable = object :Runnable{
         override fun run() {
           try{
               checkRowForThree()
               checkColumnForThree()
               moveDownCandies()
           }
           finally {
               mHandler.postDelayed(this,interval)
           }
         }

     }

    private fun startRepeat() {
     repeatChecker.run()
    }

    private fun createBoard() {
     val gridLayout = findViewById<GridLayout>(R.id.board)
        gridLayout.rowCount = noOfBlock
        gridLayout.columnCount = noOfBlock
        gridLayout.layoutParams.width = widthOfScreen
        gridLayout.layoutParams.height = widthOfScreen

        for(i in 0 until noOfBlock *noOfBlock ){

            val imageView = ImageView(this)
            imageView.id = i
            imageView.layoutParams = android.
            view.ViewGroup
                .LayoutParams(widthOfBlock,widthOfBlock)

            imageView.maxHeight = widthOfBlock
            imageView.maxWidth = widthOfBlock

            var random : Int = Math.floor(Math.random()* candies.size).toInt()

            //randomIndex from candies array
            imageView.setImageResource(candies[random])
            imageView.setTag(candies[random])

            candy.add(imageView)
            gridLayout.addView(imageView)

        }
    }
    private fun showMaxWrongMovesAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Game Over")
        builder.setMessage("You have made 5 wrong moves. Do you want to exit the game?")
        builder.setPositiveButton("Yes") { _, _ ->
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
    private fun updateWrongMoveCount() {
        wrongMovesTextView.text = " Moves: $wrongMoveCount"
    }
    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit")
        builder.setMessage("Are you sure you want to exit the game?")
        builder.setPositiveButton("Yes") { _, _ ->
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
package com.example.candycrushgame.uiltel

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

// Custom OnSwipeListener class to detect swipe gestures on a View
open class OnSwipeListener(context :Context?) :View.OnTouchListener
{
     var gestureDelector:GestureDetector
    // onTouch method to detect touch events on the View
    override fun onTouch(v: View?, motionEvent: MotionEvent): Boolean {
        return gestureDelector.onTouchEvent(motionEvent)
    }
    // Inner class to handle gesture detection
    inner class GestureListener :GestureDetector.SimpleOnGestureListener(){

        val SWIPE_THRESOLD = 100
        val SWIPE_VELOCITY_THRESOLD = 100
        // Method invoked when a touch down event occurs
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
        // Method invoked when a fling gesture occurs
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            // Calculate the horizontal and vertical distance differences
            val yDiff = e2.y - e1!!.y
            val xDiff = e2.x - e1!!.x
            //it means that we are either going
            //to left or right direction &
            //Top to bottom direction

            if(Math.abs(xDiff)> Math.abs(yDiff)){

                if(Math.abs(xDiff)>SWIPE_THRESOLD
                    && Math.abs(velocityX)>SWIPE_VELOCITY_THRESOLD){
                    if(xDiff > 0){
                        onSwipeRight()
                    }
                    else{
                        onSwipeLift()
                    }
                    result = true
                }
            }
            else if(Math.abs(yDiff)>SWIPE_THRESOLD
                && Math.abs(velocityY)>SWIPE_VELOCITY_THRESOLD){
                if(xDiff > 0){
                    onSwipeTop()
                }
                else{
                    onSwipeBottom()
                }
                result = true
            }
            return result
        }


    }
    // Open methods to be overridden by subclasses to handle specific swipe directions
    open fun onSwipeBottom() {}

    open fun onSwipeTop() {}

    open fun onSwipeLift() {}

    open fun onSwipeRight() {}
    // Initialize the GestureDetector with the provided context and GestureListener
    init {
        gestureDelector = GestureDetector(context,GestureListener())

    }


}
package com.example.pong_group.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pong_group.Model.GameViewBreakout
import com.example.pong_group.Model.GameViewPONG

//container controller for surfaceview for gamePong
class GamePong : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(GameViewPONG(this))
    }

    //override onBackPressed to stop running thread when user press on back arrow
    override fun onBackPressed() {
        super.onBackPressed()
        GameViewPONG.running = false
        try {
            GameViewPONG.thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
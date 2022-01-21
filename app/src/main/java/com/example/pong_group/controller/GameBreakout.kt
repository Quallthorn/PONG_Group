package com.example.pong_group.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pong_group.model.GameViewBreakout
import com.example.pong_group.services.GameSettings
import com.example.pong_group.services.GameSounds.playSound
import com.example.pong_group.services.Sounds.*

//container for displaying surface view for breakout game
class GameBreakout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(GameViewBreakout(this))
    }

    //stop running tread when back button pressed
    override fun onBackPressed() {
        super.onBackPressed()
        playSound(CLICK)
        GameViewBreakout.thread.running = false
        GameViewBreakout.thread.join()
    }

    //cheat-proof score, resets score if game is paused
    override fun onPause(){
        super.onPause()
        playSound(CLICK)
        GameSettings.scoreBreakout = 0
    }
}
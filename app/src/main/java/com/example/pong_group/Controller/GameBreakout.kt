package com.example.pong_group.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pong_group.Model.GameViewBreakout
import com.example.pong_group.Services.GameSounds

//container for displaying surface view for breakout game
class GameBreakout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(GameViewBreakout(this))
    }

    //stop running tread when back button pressed
    override fun onBackPressed() {
        super.onBackPressed()
        GameSounds.playClick()
        GameViewBreakout.thread.running = false
        GameViewBreakout.thread.join()
    }
}
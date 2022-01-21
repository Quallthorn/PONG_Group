package com.example.pong_group.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pong_group.model.GameViewPONG
import com.example.pong_group.services.GameSounds.playSound
import com.example.pong_group.services.Sounds.*

//container controller for surfaceview for gamePong
class GamePong : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(GameViewPONG(this))
    }

    //override onBackPressed to stop running thread when user press on back arrow
    override fun onBackPressed() {
        super.onBackPressed()
        playSound(CLICK)
        GameViewPONG.running = false
        try {
            GameViewPONG.thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
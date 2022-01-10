package com.example.pong_group.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pong_group.Model.GameViewBreakout
import com.example.pong_group.Model.GameViewPONG

class GamePong : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(GameViewPONG(this))
    }

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
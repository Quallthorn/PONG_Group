package com.example.pong_group.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.pong_group.Model.GameViewBreakout
import kotlin.concurrent.thread

class GameBreakout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(GameViewBreakout(this))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        GameViewBreakout.thread.running = false
        GameViewBreakout.thread.join()
    }
}
package com.example.pong_group.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pong_group.Model.GameViewPONG

class GameBreakout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_game_breakout)
        setContentView(GameViewPONG(this))
    }
}
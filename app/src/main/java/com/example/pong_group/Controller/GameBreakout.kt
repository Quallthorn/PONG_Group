package com.example.pong_group.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pong_group.Model.GameViewBreakout

class GameBreakout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(GameViewBreakout(this))
    }
}
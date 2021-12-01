package com.example.pong_group.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import com.example.pong_group.Model.GameViewPONG
import com.example.pong_group.R

class GameBreakout : AppCompatActivity() {

    private lateinit var surface: SurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_breakout)
        surface = findViewById(R.id.surface_view_breakout)
        setContentView(GameViewPONG(this, surface))
    }
}
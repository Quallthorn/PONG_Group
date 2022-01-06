package com.example.pong_group.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.pong_group.R

class MainActivity : AppCompatActivity() {

    lateinit var pong : ImageButton
    lateinit var breakout : ImageButton
    lateinit var highscore : ImageButton
    lateinit var settings: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pong = findViewById(R.id.pong_game_button)
        breakout = findViewById(R.id.breakout_button)
        highscore = findViewById(R.id.high_score_button)
        settings = findViewById(R.id.settings_button)

        pong.setOnClickListener{
            Intent(this, GamePong::class.java).apply { startActivity(this) }
        }

        breakout.setOnClickListener{
            Intent(this, ChooseBreakoutGame::class.java).apply { startActivity(this) }
        }

        highscore.setOnClickListener{
            Intent(this, HighScore::class.java).apply { startActivity(this) }
        }

        settings.setOnClickListener {
            Intent(this, Settings::class.java).apply { startActivity(this) }
        }
    }
}
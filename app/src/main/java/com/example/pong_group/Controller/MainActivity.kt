package com.example.pong_group.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.example.pong_group.R

class MainActivity : AppCompatActivity() {

    lateinit var pong : ImageButton
    lateinit var breakout : ImageButton
    lateinit var highscore : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pong = findViewById(R.id.game_1)
        breakout = findViewById(R.id.game_2)
        highscore = findViewById(R.id.high_score)

        pong.setOnClickListener{
            Intent(this, GamePong::class.java).apply { startActivity(this) }
        }

        breakout.setOnClickListener{
            Intent(this, GameBreakout::class.java).apply { startActivity(this) }
        }

        highscore.setOnClickListener{
            Intent(this, HighScore::class.java).apply { startActivity(this) }
        }
    }
}
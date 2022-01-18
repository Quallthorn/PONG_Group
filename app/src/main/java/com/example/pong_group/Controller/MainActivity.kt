package com.example.pong_group.Controller

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameSounds
import com.example.pong_group.Services.GameSounds.createSoundPool


//Main activity controller for navigation between activities
class MainActivity : AppCompatActivity() {

    lateinit var pong : ImageButton
    lateinit var breakout : ImageButton
    lateinit var highscore : ImageButton
    lateinit var settings: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createSoundPool(this)


        pong = findViewById(R.id.pong_game_button)
        breakout = findViewById(R.id.breakout_button)
        highscore = findViewById(R.id.high_score_button)
        settings = findViewById(R.id.settings_button)

        pong.setOnClickListener{
            GameSounds.playClick()
            Intent(this, GamePong::class.java).apply { startActivity(this) }
        }

        breakout.setOnClickListener{
            GameSounds.playClick()
            Intent(this, GameBreakout::class.java).apply { startActivity(this) }
        }

        highscore.setOnClickListener{
            GameSounds.playClick()
            Intent(this, HighScore::class.java).apply { startActivity(this) }
        }

        settings.setOnClickListener {
            GameSounds.playClick()
            Intent(this, Settings::class.java).apply { startActivity(this) }
        }
    }

    override fun onResume() {
        super.onResume()
        //change button background depend on preferences
        if (prefs.isClassicInterface)
            breakout.setBackgroundResource(R.drawable.button_classic_breakout)
        else
            breakout.setBackgroundResource(R.drawable.button_breakout)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        GameSounds.playClick()
    }
}
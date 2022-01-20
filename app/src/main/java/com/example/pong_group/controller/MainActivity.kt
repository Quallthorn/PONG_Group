package com.example.pong_group.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.pong_group.R
import com.example.pong_group.services.GameSounds.createSoundPool
import com.example.pong_group.services.GameSounds.playSound
import com.example.pong_group.services.Sounds.*


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
            playSound(CLICK)
            Intent(this, GamePong::class.java).apply { startActivity(this) }
        }

        breakout.setOnClickListener{
            playSound(CLICK)
            Intent(this, GameBreakout::class.java).apply { startActivity(this) }
        }

        highscore.setOnClickListener{
            playSound(CLICK)
            Intent(this, HighScore::class.java).apply { startActivity(this) }
        }

        settings.setOnClickListener {
            playSound(CLICK)
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
        playSound(CLICK)
    }
}
package com.example.pong_group.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.widget.AppCompatToggleButton
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameSounds

class Settings : AppCompatActivity() {
    private lateinit var muteSwitch: AppCompatToggleButton
    private lateinit var colorSwitch: AppCompatToggleButton
    private lateinit var bestOfText: EditText
    private lateinit var opponentSwitch: AppCompatToggleButton
    private lateinit var versionSwitch: AppCompatToggleButton
    private lateinit var levelSwitch: AppCompatToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        muteSwitch = findViewById(R.id.mute_switch)
        colorSwitch = findViewById(R.id.color_text)
        bestOfText = findViewById(R.id.best_of_text)
        opponentSwitch = findViewById(R.id.opponent_text)
        versionSwitch = findViewById(R.id.version_text)
        levelSwitch = findViewById(R.id.level_text)

        muteSwitch.setOnClickListener {
            GameSounds.appMuted = muteSwitch.isChecked
            GameSounds.playSound()
        }

        colorSwitch.setOnClickListener {
            GameSettings.rainbowColor = colorSwitch.isChecked
        }

        opponentSwitch.setOnClickListener {
            GameSettings.opponentP2 = opponentSwitch.isChecked
        }

        versionSwitch.setOnClickListener {
            GameSettings.classicBreakout = versionSwitch.isChecked
        }

        levelSwitch.setOnClickListener {
            GameSettings.infiniteLevel = levelSwitch.isChecked
            if (levelSwitch.isChecked){
                versionSwitch.isEnabled = false
                GameSettings.classicBreakout = !levelSwitch.isChecked
                versionSwitch.isChecked = !levelSwitch.isChecked
            }
            else
                versionSwitch.isEnabled = true

        }
    }

    override fun onResume() {
        super.onResume()
        muteSwitch.isChecked = GameSounds.appMuted
        colorSwitch.isChecked = GameSettings.rainbowColor
        bestOfText.setText(GameSettings.bestOf.toString())
        opponentSwitch.isChecked = GameSettings.opponentP2
        versionSwitch.isChecked = GameSettings.classicBreakout
        levelSwitch.isChecked = GameSettings.infiniteLevel
        if (levelSwitch.isChecked)
            versionSwitch.isEnabled = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        GameSettings.bestOf =  bestOfText.text.toString().toInt()
        GameSettings.saveSettings()
    }
}
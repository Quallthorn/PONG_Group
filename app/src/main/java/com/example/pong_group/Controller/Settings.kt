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
            prefs.isGameSoundOn = muteSwitch.isChecked
            GameSounds.playSound()
        }

        colorSwitch.setOnClickListener {
           prefs.isRainbowColor = colorSwitch.isChecked
        }

        opponentSwitch.setOnClickListener {
            prefs.isP2Human = opponentSwitch.isChecked
        }

        versionSwitch.setOnClickListener {
            prefs.isClassicInterface = versionSwitch.isChecked
        }

        levelSwitch.setOnClickListener {
            prefs.isInfiniteLevels= levelSwitch.isChecked
            if (levelSwitch.isChecked){
                versionSwitch.isEnabled = false
                prefs.isClassicInterface = !levelSwitch.isChecked
                versionSwitch.isChecked = !levelSwitch.isChecked
            }
            else
                versionSwitch.isEnabled = true

        }
    }

    override fun onResume() {
        super.onResume()
        muteSwitch.isChecked = prefs.isGameSoundOn
        colorSwitch.isChecked = prefs.isRainbowColor
        bestOfText.setText(prefs.bestOfPongPrefs.toString())
        opponentSwitch.isChecked = prefs.isP2Human
        versionSwitch.isChecked = prefs.isClassicInterface
        levelSwitch.isChecked = prefs.isInfiniteLevels
        if (levelSwitch.isChecked)
            versionSwitch.isEnabled = false
//        muteSwitch.isChecked = GameSounds.appMuted
//        colorSwitch.isChecked = GameSettings.rainbowColor
//        bestOfText.setText(GameSettings.bestOf.toString())
//        opponentSwitch.isChecked = GameSettings.opponentP2
//        versionSwitch.isChecked = GameSettings.classicBreakout
//        levelSwitch.isChecked = GameSettings.infiniteLevel
//        if (levelSwitch.isChecked)
//            versionSwitch.isEnabled = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        prefs.bestOfPongPrefs = bestOfText.text.toString().toInt()
//        GameSettings.saveSettings()
    }
}
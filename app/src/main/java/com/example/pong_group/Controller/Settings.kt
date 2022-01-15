package com.example.pong_group.Controller

import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatToggleButton
import com.example.pong_group.R
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

        bestOfText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    if (bestOfText.text.isNotBlank()){
                        prefs.bestOfPongPrefs = bestOfText.text.toString().toInt()
                    }
                    //Toast.makeText(applicationContext, "saved", Toast.LENGTH_SHORT).show()
                    return true
                }
                return false
            }
        })

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
    }
}
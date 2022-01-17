package com.example.pong_group.Controller

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatToggleButton
import com.example.pong_group.R
import com.example.pong_group.Services.GameSounds

//Controller for activity settings
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

        //bind view for activity
        muteSwitch = findViewById(R.id.mute_switch)
        colorSwitch = findViewById(R.id.color_text)
        bestOfText = findViewById(R.id.best_of_text)
        opponentSwitch = findViewById(R.id.opponent_text)
        versionSwitch = findViewById(R.id.version_text)
        levelSwitch = findViewById(R.id.level_text)


        //turn on/off sound
        muteSwitch.setOnClickListener {
            prefs.isGameMute = muteSwitch.isChecked
            GameSounds.playSoundWall()
        }

        //change color scheme for paddle and ball
        colorSwitch.setOnClickListener {
           prefs.isRainbowColor = colorSwitch.isChecked
        }

        //set max point to win  for PONG game
        bestOfText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    if (bestOfText.text.isNotBlank()){
                        prefs.firstToPongPrefs = bestOfText.text.toString().toInt()
                    }
                    return true
                }
                return false
            }
        })

        //change player between himan and cpu
        opponentSwitch.setOnClickListener {
            prefs.isP2Human = opponentSwitch.isChecked
        }

        //switch breakout between classic interface and standart
        versionSwitch.setOnClickListener {
            prefs.isClassicInterface = versionSwitch.isChecked
        }

        //switch between two levels game and infinite levels
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

        //setup settings preferences based on saved shared preferences
        muteSwitch.isChecked = prefs.isGameMute
        colorSwitch.isChecked = prefs.isRainbowColor
        bestOfText.setText(prefs.firstToPongPrefs.toString())
        opponentSwitch.isChecked = prefs.isP2Human
        versionSwitch.isChecked = prefs.isClassicInterface
        levelSwitch.isChecked = prefs.isInfiniteLevels
        if (levelSwitch.isChecked)
            versionSwitch.isEnabled = false
    }
}
package com.example.pong_group.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SwitchCompat
import com.example.pong_group.R
import com.example.pong_group.Services.GameSounds

class Settings : AppCompatActivity() {
    lateinit var sound_switch_button: SwitchCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        sound_switch_button = findViewById(R.id.sound_switch)

        sound_switch_button.setOnClickListener {
            GameSounds.isSoundOn = sound_switch_button.isChecked
            GameSounds.playSound()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
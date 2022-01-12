package com.example.pong_group.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatToggleButton
import com.example.pong_group.R
import com.example.pong_group.Services.GameSounds

class Settings : AppCompatActivity() {
    private lateinit var muteSwitch: AppCompatToggleButton
    private lateinit var versionSwitch: AppCompatToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        muteSwitch = findViewById(R.id.mute_switch)

        muteSwitch.isChecked = GameSounds.appMuted

        muteSwitch.setOnClickListener {
            GameSounds.appMuted = muteSwitch.isChecked
            GameSounds.playSound()
        }

        versionSwitch = findViewById(R.id.version_switch)
    }
}
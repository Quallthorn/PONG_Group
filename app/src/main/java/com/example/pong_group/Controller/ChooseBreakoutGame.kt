package com.example.pong_group.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings

class ChooseBreakoutGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_breakout_game)

        val modernButton: ImageButton = findViewById(R.id.modern_breakout_button)
        val classicButton: ImageButton = findViewById(R.id.classic_breakout_button)

        modernButton.setOnClickListener{
            prefs.isClassicInterface = false
            Intent(this, GameBreakout::class.java).apply { startActivity(this) }
        }

        classicButton.setOnClickListener{
            prefs.isClassicInterface = true
            Intent(this, GameBreakout::class.java).apply { startActivity(this) }
        }
    }
}
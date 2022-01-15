package com.example.pong_group.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.example.pong_group.Model.ScoresRealm
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.SharedBreakout

class NameInputActivity : AppCompatActivity() {

    private lateinit var rank: TextView
    private lateinit var score: TextView
    private lateinit var nameInitials: EditText
    private lateinit var submitButton: TextView
    private lateinit var cancelButton: TextView
    private lateinit var warning: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name_input)

        rank = findViewById(R.id.rank_output)
        score = findViewById(R.id.score_output)
        nameInitials = findViewById(R.id.name_input)
        submitButton = findViewById(R.id.submit)
        cancelButton = findViewById(R.id.cancel)
        warning = findViewById(R.id.warning)

        score.text = GameSettings.scoreBreakout.toString()
        if (SharedBreakout.highScoreBroken)
            score.setTextColor(App.instance.resources.getColor(R.color.yellow))
        when {
            prefs.isClassicInterface -> rank.text =
                ScoresRealm.findRank(GameSettings.scoreBreakout, "classic").toString()
            prefs.isInfiniteLevels -> rank.text =
                ScoresRealm.findRank(GameSettings.scoreBreakout, "infinite").toString()
            else -> rank.text =
                ScoresRealm.findRank(GameSettings.scoreBreakout, "breakout").toString()
        }

        submitButton.setOnClickListener {
            if (nameInitials.text.length == 3) {
                when {
                    prefs.isClassicInterface ->
                        ScoresRealm.addScores(
                            GameSettings.scoreBreakout,
                            nameInitials.text.toString(),
                            "classic"
                        )

                    prefs.isInfiniteLevels ->
                        ScoresRealm.addScores(
                            GameSettings.scoreBreakout,
                            nameInitials.text.toString(),
                            "infinite"
                        )

                    else -> ScoresRealm.addScores(
                        GameSettings.scoreBreakout,
                        nameInitials.text.toString(),
                        "breakout"
                    )
                }
                resetScore()
                Intent(this, HighScore::class.java).apply { startActivity(this) }
            } else {
                warning.visibility = View.VISIBLE
            }
        }
        cancelButton.setOnClickListener {
            backToMainMenu()
            resetScore()
        }
    }

    override fun onResume() {
        super.onResume()
        warning.visibility = View.INVISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        backToMainMenu()
    }

    private fun backToMainMenu() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun resetScore() {
        SharedBreakout.highScoreBroken = false
        GameSettings.scoreBreakout = 0
    }
}
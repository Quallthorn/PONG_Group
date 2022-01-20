package com.example.pong_group.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.example.pong_group.model.GameType
import com.example.pong_group.model.ScoresRealm
import com.example.pong_group.R
import com.example.pong_group.services.GameSettings.savedScore
import com.example.pong_group.services.GameSounds.playSound
import com.example.pong_group.services.SharedBreakout
import com.example.pong_group.services.Sounds.*

// activity for fixing result, players name and send result to database
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

        score.text = savedScore.toString()

        //find position in table in database and show it to user
        if (SharedBreakout.highScoreBroken)
            score.setTextColor(App.instance.resources.getColor(R.color.yellow, App.instance.theme))
        when {
            prefs.isClassicInterface -> rank.text =
                ScoresRealm.findRank(savedScore, GameType.CLASSIC).toString()
            prefs.isInfiniteLevels -> rank.text =
                ScoresRealm.findRank(savedScore, GameType.INFINITE).toString()
            else -> rank.text =
                ScoresRealm.findRank(savedScore, GameType.BREAKOUT).toString()
        }

        //send scores to database and transition to highscores
        submitButton.setOnClickListener {
            if (nameInitials.text.length == 3) {
                playSound(CLICK)
                when {
                    prefs.isClassicInterface ->
                        ScoresRealm.addScores(
                            savedScore,
                            nameInitials.text.toString(),
                            GameType.CLASSIC
                        )

                    prefs.isInfiniteLevels ->
                        ScoresRealm.addScores(
                            savedScore,
                            nameInitials.text.toString(),
                            GameType.INFINITE
                        )

                    else -> ScoresRealm.addScores(
                        savedScore,
                        nameInitials.text.toString(),
                        GameType.BREAKOUT
                    )
                }
                resetScore()
                Intent(this, HighScore::class.java).apply { startActivity(this) }
            } else {
                warning.visibility = View.VISIBLE
            }
        }

        //cancel button to return back to game
        cancelButton.setOnClickListener {
            playSound(CLICK)
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
        //TODO: check if it good practice to do like that
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun resetScore() {
        SharedBreakout.highScoreBroken = false
        savedScore = 0
    }
}
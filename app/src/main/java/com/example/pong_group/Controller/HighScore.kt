package com.example.pong_group.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pong_group.Controller.App.Companion.instance
import com.example.pong_group.Model.GameType
import com.example.pong_group.Model.Scores
import com.example.pong_group.Model.ScoresRealm
import com.example.pong_group.R
import com.example.pong_group.adapters.HighScoreAdapter

class HighScore : AppCompatActivity() {

    var scoresList = mutableListOf<Scores>()
    private lateinit var titleText: TextView
    lateinit var scoresRadioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)

        titleText = findViewById(R.id.game_title)

        val classicRadioButton = findViewById<RadioButton>(R.id.classicRadioButton)
        val infiniteRadioButton = findViewById<RadioButton>(R.id.infiniteRadioButton)
        val breakoutRadioButton = findViewById<RadioButton>(R.id.breakoutRadioButton)

        titleText.text = when{
            prefs.isClassicInterface -> instance.getString(R.string.classic).also {
                scoresList =  ScoresRealm.retrieveScores(GameType.CLASSIC)
                classicRadioButton.isChecked = true

            }
            prefs.isInfiniteLevels -> instance.getString(R.string.infinite).also {
                scoresList =  ScoresRealm.retrieveScores(GameType.INFINITE)
                infiniteRadioButton.isChecked = true
            }
            else -> instance.getString(R.string.breakout).also {
                scoresList =  ScoresRealm.retrieveScores(GameType.BREAKOUT)
                breakoutRadioButton.isChecked = true
            }
        }
//
//        scoresList = when {
//            prefs.isClassicInterface -> ScoresRealm.retrieveScores(GameType.CLASSIC)
//            prefs.isInfiniteLevels -> ScoresRealm.retrieveScores(GameType.INFINITE)
//            else -> ScoresRealm.retrieveScores(GameType.BREAKOUT)
//        }


        val listview = findViewById<RecyclerView>(R.id.scores_list)
        val adapter = HighScoreAdapter(scoresList)
        listview.adapter = adapter
        listview.layoutManager = LinearLayoutManager(instance)

        scoresRadioGroup = findViewById(R.id.scoresRadioGroup)

        scoresRadioGroup.setOnCheckedChangeListener { radioGroup, radioButtonID ->
            val selectedRadioButton = radioGroup.findViewById<RadioButton>(radioButtonID)
            var list: MutableList<Scores> = mutableListOf()
            when (selectedRadioButton.text) {
                "CLASSIC" -> ScoresRealm.retrieveScores(GameType.CLASSIC).also {
                    list = it
                    titleText.text = "CLASSIC"}
                "INFINITE" -> ScoresRealm.retrieveScores(GameType.INFINITE).also {
                    list = it
                    titleText.text = "INFINITE"}
                else -> ScoresRealm.retrieveScores(GameType.BREAKOUT).also {
                    list = it
                    titleText.text = "BREAKOUT"}
            }
            scoresList
            scoresList.clear()
            scoresList.addAll(list)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
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

    //high scores properties: Scores list, name for current game and button for control game
    var scoresList = mutableListOf<Scores>()
    private lateinit var titleText: TextView
    lateinit var scoresRadioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)

        //binding variables to activity
        titleText = findViewById(R.id.game_title)
        val classicRadioButton = findViewById<RadioButton>(R.id.classicRadioButton)
        val infiniteRadioButton = findViewById<RadioButton>(R.id.infiniteRadioButton)
        val breakoutRadioButton = findViewById<RadioButton>(R.id.breakoutRadioButton)
        scoresRadioGroup = findViewById(R.id.scoresRadioGroup)
        val listview = findViewById<RecyclerView>(R.id.scores_list)

        //setup title view, retrieve scores info from list and choose current button
        titleText.text = when {
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

        //setup list adapter
        val adapter = HighScoreAdapter(scoresList)
        listview.adapter = adapter
        listview.layoutManager = LinearLayoutManager(instance)

        // radio button control for load list from base and reload list information
        scoresRadioGroup.setOnCheckedChangeListener { radioGroup, radioButtonID ->
            val selectedRadioButton = radioGroup.findViewById<RadioButton>(radioButtonID)
            var list: MutableList<Scores>
            when (selectedRadioButton.text) {
                "CLASSIC" -> ScoresRealm.retrieveScores(GameType.CLASSIC).also {
                    list = it
                    titleText.text = resources.getString(R.string.classic)}
                "INFINITE" -> ScoresRealm.retrieveScores(GameType.INFINITE).also {
                    list = it
                    titleText.text = resources.getString(R.string.infinite)}
                else -> ScoresRealm.retrieveScores(GameType.BREAKOUT).also {
                    list = it
                    titleText.text = resources.getString(R.string.breakout)}
            }
            scoresList
            scoresList.clear()
            scoresList.addAll(list)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //TODO: check if it properly way to go back
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
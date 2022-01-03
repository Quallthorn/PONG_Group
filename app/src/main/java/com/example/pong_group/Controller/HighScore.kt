package com.example.pong_group.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pong_group.Controller.App.Companion.instance
import com.example.pong_group.Model.Scores
import com.example.pong_group.Model.ScoresRealm
import com.example.pong_group.R
import com.example.pong_group.adapters.HighScoreAdapter

class HighScore : AppCompatActivity() {

    var scoresList = mutableListOf<Scores>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)

        scoresList = ScoresRealm.retrieveScores("breakout")
        val listview = findViewById<RecyclerView>(R.id.scores_list)
        val adapter = HighScoreAdapter(scoresList)
        listview.adapter = adapter
        listview.layoutManager = LinearLayoutManager(instance)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
}
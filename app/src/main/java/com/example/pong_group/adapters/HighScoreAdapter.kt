package com.example.pong_group.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pong_group.controller.App.Companion.instance
import com.example.pong_group.model.Scores
import com.example.pong_group.R


// custom adapter for  manage high score list and show it properly
class HighScoreAdapter (private val highScores: List<Scores>): RecyclerView.Adapter<HighScoreAdapter.ScoresHolder>()
{
    inner class  ScoresHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val rankTV: TextView = itemView.findViewById(R.id.list_rank)
        private val nameTV: TextView = itemView.findViewById(R.id.list_name)
        private val scoresTV: TextView = itemView.findViewById(R.id.list_scores)

        //binding func for bind scores from list to table row
        fun bindScores(scores: Scores, position: Int) {
            rankTV.text  = (position+1).toString()
            nameTV.text = scores.name
            scoresTV.text = scores.scores.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoresHolder {
        val view = LayoutInflater.from(instance)
            .inflate(R.layout.game_scores_list_item, parent, false)
        return ScoresHolder(view)
    }

    override fun onBindViewHolder(holder: ScoresHolder, position: Int) {
        holder.bindScores(highScores[position], position)
    }

    override fun getItemCount(): Int {
        return  highScores.count()
    }
}
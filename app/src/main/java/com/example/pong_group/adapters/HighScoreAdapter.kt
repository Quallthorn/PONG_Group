package com.example.pong_group.adapters

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pong_group.Controller.App.Companion.instance
import com.example.pong_group.Model.Scores
import com.example.pong_group.R


// custom adapter for  manage high score list and show it properly
class HighScoreAdapter (val highScores: List<Scores>): RecyclerView.Adapter<HighScoreAdapter.ScoresHolder>()
{
    inner class  ScoresHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

        val rankTV: TextView = itemView.findViewById(R.id.list_rank)
        val nameTV: TextView = itemView.findViewById(R.id.list_name)
        val scoresTV: TextView = itemView.findViewById(R.id.list_scores)

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
package com.example.pong_group.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.example.pong_group.R

class NameInputActivity : AppCompatActivity() {

    lateinit var rank: TextView
    lateinit var score: TextView
    lateinit var nameInitials: EditText
    lateinit var submitButton: TextView
    lateinit var warning: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name_input)

        rank = findViewById(R.id.rank_output)
        score = findViewById(R.id.score_output)
        nameInitials = findViewById(R.id.name_input)
        submitButton = findViewById(R.id.submit)
        warning = findViewById(R.id.warning)

        submitButton.setOnClickListener{
            if (nameInitials.text.length == 3){
                Intent(this, HighScore::class.java).apply { startActivity(this) }
            }
            else{
                warning.visibility = View.VISIBLE
            }
        }

    }

    override fun onResume() {
        super.onResume()
        warning.visibility = View.INVISIBLE
    }
}
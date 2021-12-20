package com.example.pong_group.Model

import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.pong_group.Controller.App
import com.example.pong_group.R
import com.example.pong_group.Model.GameViewBreakout.Companion.canvasBreakout
import com.example.pong_group.Services.GameSettings

class PaddleBreakout() {

    var posX: Float = 500f
    var posY: Float = 1f
    var height: Float = 25f
    var width: Float = 80f
    var paint = Paint()

    init {
        this.paint.color = App.instance.resources.getColor(R.color.light_blue)
    }

    fun update(){
    }

    fun draw (){
        canvasBreakout?.drawRect(
            posX - width,
            GameSettings.screenHeight - posY,
            posX + width,
            GameSettings.screenHeight - posY + height,
            paint
        )
    }

    fun halfSize(){
        width *= 0.5f
    }
}
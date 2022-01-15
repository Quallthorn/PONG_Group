package com.example.pong_group.Model

import com.example.pong_group.Controller.App
import com.example.pong_group.Controller.prefs
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameSettings.curCanvas

class PaddleBreakout: BasicPaddle() {

    init {
        if (prefs.isRainbowColor)
            this.paint = GameSettings.curPaint
        else
            this.paint.color = App.instance.resources.getColor(R.color.light_blue, App.instance.theme)
        width = 80f
        height = 25f
    }

    fun draw (){
        curCanvas.drawRect(
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
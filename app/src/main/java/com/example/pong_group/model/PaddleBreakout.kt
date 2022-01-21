package com.example.pong_group.model

import com.example.pong_group.controller.App
import com.example.pong_group.controller.prefs
import com.example.pong_group.R
import com.example.pong_group.services.GameSettings
import com.example.pong_group.services.GameSettings.curCanvas

class PaddleBreakout: BasicPaddle() {

    init {
        if (prefs.isRainbowColor)
            this.paint = GameSettings.curPaint
        else
            this.paint.color = App.instance.resources.getColor(R.color.light_blue, App.instance.theme)
        width = 80f
        height = 25f
    }

    /**
     * draws paddle to canvas declared in GameSettings
     */
    fun draw (){
        curCanvas.drawRect(
            posX - width,
            GameSettings.screenHeight - posY,
            posX + width,
            GameSettings.screenHeight - posY + height,
            paint
        )
    }

    /**
     * halves the size of the paddle
     *
     * used only in classic mode
     */
    fun halfSize(){
        width *= 0.5f
    }
}
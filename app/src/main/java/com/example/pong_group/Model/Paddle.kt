package com.example.pong_group.Model

import android.graphics.Paint
import com.example.pong_group.Controller.App
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameSettings.curCanvas
import com.example.pong_group.Services.NumberPrinter

class Paddle(isCpu: Boolean) {

    var posX: Float = 500f
    var posY: Float = 1f
    var height: Float = 20f
    var width: Float = 40f
    var paint = Paint()
    var scorePositionXL = 0f
    var scorePositionXR = 0f
    var scorePositionY = 0f

    var posXOld: Float = 0f
    private val frameCheck = 2
    var currentFrame = 0
    var isCpu = isCpu

    init {
        this.paint.color = App.instance.resources.getColor(R.color.white)
    }

    companion object{
        var cpuScore = 0
        var playerScore = 0
        var absoluteScore = 0
    }


    fun update(){
        if (currentFrame == frameCheck){
            posXOld = posX
            currentFrame = 0
        }
        else
            currentFrame++
    }

    fun draw (){
        curCanvas.drawRect(
            posX - width,
            GameSettings.screenHeight - posY,
            posX + width,
            GameSettings.screenHeight - posY + height,
            paint
        )

        if(isCpu) {
            NumberPrinter.drawNumber(playerScore, scorePositionXL, scorePositionY, scorePositionXR)
        } else {
            if (cpuScore <= 9)
                NumberPrinter.drawNumber(cpuScore, scorePositionXL, scorePositionY, scorePositionXR)
            else
                NumberPrinter.drawNumber(cpuScore, scorePositionXR, scorePositionY, scorePositionXL)
        }
    }
}
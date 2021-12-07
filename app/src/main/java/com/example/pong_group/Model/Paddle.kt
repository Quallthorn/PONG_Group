package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.example.pong_group.Controller.App
import com.example.pong_group.Model.GameViewPONG.Companion.canvas
import com.example.pong_group.R

class Paddle( width: Float, height: Float, isCpu: Boolean) {

    var posX: Float = 500f
    var posY: Float = 1f
    var height: Float = 10f
    var width: Float = 80f
    var paint = Paint()
    var scores = 0
    var scorePositionX = 0f
    var scorePositionY = 0f

    var screenWidth = width
    var screenHeight = height

    var posXOld: Float = 0f
    val frameCheck = 2
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
        canvas.drawRect(
            posX - width,
            screenHeight - posY,
            posX + width,
            screenHeight - posY + height,
            paint
        )

        if(isCpu) {
            NumberPrinter.drawNumber(playerScore, scorePositionX, scorePositionY)
        } else {
            NumberPrinter.drawNumber(cpuScore, scorePositionX, scorePositionY)
        }

    }
}
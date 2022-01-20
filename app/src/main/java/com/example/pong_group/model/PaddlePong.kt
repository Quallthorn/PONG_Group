package com.example.pong_group.model

import com.example.pong_group.services.GameSettings
import com.example.pong_group.services.GameSettings.curCanvas
import com.example.pong_group.services.NumberPrinter

class PaddlePong(var isCpu: Boolean): BasicPaddle() {

    var scorePositionXL = 0f
    var scorePositionXR = 0f
    var scorePositionY = 0f

    var posXOld: Float = 0f
    private val frameCheck = 2
    var currentFrame = 0

    init {
        this.paint = GameSettings.curPaint
    }

    companion object{
        var playerScore = 0
        var cpuScore = 0
        var absoluteScore = 0
    }

    /**
     * checks position of paddle periodically based on frameCheck int
     *
     * used when checking player movement speed
     */
    fun update(){
        if (currentFrame == frameCheck){
            posXOld = posX
            currentFrame = 0
        }
        else
            currentFrame++
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

        if(isCpu) {
            NumberPrinter.drawNumber(cpuScore, scorePositionXL, scorePositionY, scorePositionXR)
        } else {
            if (playerScore <= 9)
                NumberPrinter.drawNumber(playerScore, scorePositionXL, scorePositionY, scorePositionXR)
            else
                NumberPrinter.drawNumber(playerScore, scorePositionXR, scorePositionY, scorePositionXL)
        }
    }
}
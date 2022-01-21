package com.example.pong_group.model

import com.example.pong_group.services.GameSettings
import kotlin.math.abs
import kotlin.math.sqrt

open class BasicBall {
    var radius = 7f
    var posX = GameSettings.screenWidth * 0.5f
    var posY = GameSettings.screenHeight * 0.5f
    var dirX = (-50..50).random() / 100f
    var dirY = sqrt((1 - dirX * dirX))
    var paint = GameSettings.curPaint
    var speed = 10f

    var letGo = false
    var changeColor = false

    /**
     * draws ball on canvas
     * canvas is determined in GameSettings
     * position is determined by ball
     */
    open fun draw() {
        GameSettings.curCanvas.drawCircle(posX, posY, radius, paint)
    }

    /**
     * tells ball to change color every time it hits something if "prefs.isRainbowColor" is true
     */
    fun changeColor() {
        changeColor = true
    }

    /**
     * makes horizontal movement positive (right)
     */
    fun dirPositiveX(){
        dirX = abs(dirX)
    }

    /**
     * makes horizontal movement negative (left)
     */
    fun dirNegativeX(){
        dirX = abs(dirX) * -1
    }

    /**
     * makes vertical movement positive (down)
     */
    fun dirPositiveY(){
        dirY = abs(dirY)
    }

    /**
     * makes vertical movement negative (up)
     */
    fun dirNegativeY(){
        dirY = abs(dirY) * -1
    }
}
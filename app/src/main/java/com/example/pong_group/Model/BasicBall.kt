package com.example.pong_group.Model

import com.example.pong_group.Services.GameSettings
import kotlin.math.abs
import kotlin.math.sqrt

open class BasicBall {
    var radius = 7f
    var posX = GameSettings.screenWidth * 0.5f
    var posY = GameSettings.screenHeight * 0.5f
    var dirX = (-100..100).random() / 100f
    var dirY = sqrt((1 - dirX * dirX))
    var paint = GameSettings.curPaint
    var speed = 10f

    var letGo = false
    var changeColor = false

    open fun draw() {
        GameSettings.curCanvas.drawCircle(posX, posY, radius, paint)
    }

     fun changeColor() {
        changeColor = true
    }

    fun dirPositiveX(){
        dirX = abs(dirX)
    }

    fun dirNegativeX(){
        dirX = abs(dirX) * -1
    }

    fun dirPositiveY(){
        dirY = abs(dirY)
    }

    fun dirNegativeY(){
        dirY = abs(dirY) * -1
    }
}
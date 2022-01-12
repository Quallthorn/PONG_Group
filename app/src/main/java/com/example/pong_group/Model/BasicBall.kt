package com.example.pong_group.Model

import android.graphics.Canvas
import android.graphics.Paint
import com.example.pong_group.Services.GameSettings

open class BasicBall() {
    var radius = 7f
    var posX = GameSettings.screenWidth * 0.5f
    var posY = GameSettings.screenHeight * 0.5f
    var dirX = 0.5f
    var dirY = 0.5f
    var paint = Paint()
    var speed = 10f

    var changeColor = false

    open fun draw() {
        GameSettings.curCanvas.drawCircle(posX, posY, radius, paint)
    }

     fun changeColor() {
        changeColor = true
    }
}
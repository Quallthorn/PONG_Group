package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log

class Brick(w: Float, h: Float, x: Float, y: Float) {
    var posX = 0f
    var posY = 0f
    var width = 0f
    var height = 0f
    var paint = Paint()

    init {
        posX = x
        posY = y
        width = w
        height = h
    }

    fun update(bSize: Float, bPosX: Float, bPosY: Float){
        if (bPosX - bSize >= posX
            && bPosX + bSize <= posX + width
            && bPosY + bSize >= posY
            && bPosY - bSize <= posY + height){
            paint.color = Color.WHITE
        }
    }

    fun draw(canvas: Canvas){
        canvas?.drawRect(
            posX,
            posY,
            posX + width,
            posY + height,
            paint
        )
    }
}
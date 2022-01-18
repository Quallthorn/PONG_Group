package com.example.pong_group.views

import android.graphics.RectF

import android.graphics.Bitmap
import android.graphics.Matrix
import com.example.pong_group.Services.GameSettings.curCanvas


class SurfaceViewButton(private val bg: Bitmap) {

    var btnMatrix: Matrix = Matrix()

    var btnRect: RectF? = null
    var width = 0f

    init {
        btnRect = RectF(0f, 0f, bg.width.toFloat(), bg.height.toFloat())
        width = bg.width.toFloat()
    }

    /**
     * declares positioning for button
     */
    fun setPosition(x: Float, y: Float) {
        val drawableRect = RectF(0f,0f, bg.width.toFloat(), bg.height.toFloat())
        btnMatrix.setRectToRect(drawableRect, btnRect, Matrix.ScaleToFit.FILL)
        btnMatrix.setTranslate(x, y)
        btnMatrix.mapRect(btnRect)
    }

    /**
     * draws paddle to canvas declared in GameSettings
     */
    fun draw() {
        curCanvas.drawBitmap(bg, btnMatrix, null)
    }
}
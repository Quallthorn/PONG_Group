package com.example.pong_group.views

import android.graphics.RectF

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix


class SurfaceViewButton(val bg: Bitmap) {

    var btn_matrix: Matrix = Matrix()

    var btn_rect: RectF? = null
    var width = 0f

    init {
        btn_rect = RectF(0f, 0f, bg.width.toFloat(), bg.height.toFloat())
        width = bg.width.toFloat()
    }

    fun setPosition(x: Float, y: Float) {
        val drawableRect = RectF(0f,0f, bg.width.toFloat(), bg.height.toFloat())
        btn_matrix.setRectToRect(drawableRect, btn_rect, Matrix.ScaleToFit.FILL)
        btn_matrix.setTranslate(x, y)
        btn_matrix.mapRect(btn_rect)
    }

    fun draw(canvas: Canvas) {
       canvas.drawBitmap(bg, btn_matrix, null)
    }
}
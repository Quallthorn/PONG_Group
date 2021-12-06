package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint

class Ball(context: Context, width: Float, height: Float) {
    private var size = 5f
    var posX = size
    var posY = size
    var paint = Paint()
    var speed = 10f

    var dirX = 0.5f
    var dirY = 0.5f

    var screenWidth = width
    var screenHeight = height

    var draw = true
    val anglesCount = 10 // max 10 possibly 10.9 but not recommended

    init {
        posX = screenWidth * 0.5f
        posY = screenHeight * 0.5f
    }


    fun update(pPosX: Float, pPosY: Float, pWidth: Float, pHeight: Float, pOldX: Float) {
        if (draw) {
            if (posX >= screenWidth - size || posX <= size)
                dirX *= -1
            if (posY >= screenHeight - size) {
                dirY = dirY * -1f
                //draw = false
            } else if (posY <= size)
                dirY = dirY * -1f

            if (posY >= screenHeight - pPosY - size
                && posY <= screenHeight - pPosY + pHeight
                && posX >= pPosX - pWidth
                && posX <= pPosX + pWidth
            ) {
                for (i in 1 until anglesCount) {
                    if (posX >= pPosX - (pWidth / anglesCount) * (anglesCount + 1 - i) && posX <= pPosX - (pWidth / anglesCount) * (anglesCount - i))
                        dirX = -(anglesCount - i) / 10f
                    else if (posX <= pPosX + (pWidth / anglesCount) * (anglesCount + 1 - i) && posX >= pPosX + (pWidth / anglesCount) * (anglesCount - i)) {
                        dirX = (anglesCount - i) / 10f
                    }
                }
                dirY = -Math.sqrt((1 - dirX * dirX).toDouble()).toFloat()

                if (Math.abs(pPosX - pOldX) > 20f)
                    speed += 10f
                else
                    speed = 10f
            }

//            if (posY >= screenHeight - pPosY - size
//                && posY <= screenHeight - pPosY + pHeight
//                && posX >= pPosX - pWidth
//                && posX <= pPosX + pWidth
//            ) {
//                dirY = dirY * -1

//                if (Math.abs(pPosX - pOldX) > 5f) {
//                    // [old] --> [new]
//                    if (pPosX - pOldX > 0) {
//                        // o -->
//                        if (dirX >= 0)
//                            dirX += pPosX - pOldX
//                        // <-- o
//                        else
//                            dirX = dirX * -1
//                    }
//                    // [new] <-- [old]
//                    else if (pPosX - pOldX < 0)
//                    // <-- o
//                    if (dirX < 0)
//                        dirX -= pPosX - pOldX
//                    // o -->
//                    else
//                        dirX = dirX * -1
//                    if (dirX > 1.0f)
//                        dirX = 0.8f
//                    else if (dirX < -1.0f)
//                        dirX = -0.8f
//
//                    dirY = -Math.sqrt((1 - dirX * dirX).toDouble()).toFloat()
//                }
//            }

            posX += speed * dirX
            posY += speed * dirY
        }
    }

    fun draw(canvas: Canvas?) {
        if (draw)
            canvas?.drawCircle(posX, posY, size, paint)
    }

    fun setSize(size: Float) {
        this.posX += size - this.size
        this.posY += size - this.size
        this.size = size
    }
}
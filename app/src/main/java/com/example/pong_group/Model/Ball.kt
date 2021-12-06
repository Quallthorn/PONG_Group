package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.media.MediaPlayer
import com.example.pong_group.R

class Ball(context: Context, width: Float, height: Float) {
    private var size = 10f
    var posX = size
    var posY = size
    var paint = Paint()
    var speed = 10f
    var context = context

    var dirX = 0.5f
    var dirY = 0.5f

    var screenWidth = width
    var screenHeight = height

    var draw = true
    val anglesCount = 10 // max 10 possibly 10.9 but not recommended
    val pongSoung = MediaPlayer.create(context, R.raw.pong_sound)
    val maxSpeed = 40f

    init {
        posX = screenWidth * 0.5f
        posY = screenHeight * 0.5f
    }


    fun update(
        pPosX: Float,
        pPosY: Float,
        pWidth: Float,
        pHeight: Float,
        pOldX: Float,
        CPUX: Float
    ) {
        if (draw) {
            if (posX >= screenWidth - size)
            {
                playSound()
                dirX = Math.abs(dirX) * -1
            }
            else if (posX <= size)
                dirX = Math.abs(dirX)
            if (posY >= screenHeight - size) {
                dirY = Math.abs(dirY) * -1
                playSound()
                //draw = false
            } else if (posY <= size)
                dirY = Math.abs(dirY)

            if (posY >= screenHeight - pPosY - size
                && posY <= screenHeight - pPosY + speed
                && posX >= pPosX - pWidth
                && posX <= pPosX + pWidth
            ) {
                bounceP1(pPosX, pWidth, pOldX)
            }

            if (posY <= pPosY + size
                && posY >= pPosY - speed
                && posX >= CPUX - pWidth
                && posX <= CPUX + pWidth
            ) {
                bounceCPU(CPUX, pWidth)
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

    fun bounceP1(pPosX: Float, pWidth: Float, pOldX: Float) {
        for (i in 1 until anglesCount) {
            if (posX >= pPosX - (pWidth / anglesCount) * (anglesCount + 1 - i) && posX <= pPosX - (pWidth / anglesCount) * (anglesCount - i))
                dirX = -(anglesCount - i) / 10f
            else if (posX <= pPosX + (pWidth / anglesCount) * (anglesCount + 1 - i) && posX >= pPosX + (pWidth / anglesCount) * (anglesCount - i)) {
                dirX = (anglesCount - i) / 10f
            }
        }
        dirY = -1 * dirY / Math.abs(dirY) * Math.sqrt((1 - dirX * dirX).toDouble()).toFloat()
        playSound()
        //dirY = -1 * dirY / Math.abs(dirY) * Math.sqrt((1 - dirX * dirX).toDouble()).toFloat()
        dirY = -Math.sqrt((1 - dirX * dirX).toDouble()).toFloat()

        if (speed < maxSpeed) {
            if (Math.abs(pPosX - pOldX) > screenWidth / 54)
                speed += 5f
            else
                speed += 0.1f

            if (speed < maxSpeed)
                speed == maxSpeed
        }
    }

    fun bounceCPU(CPUX: Float, pWidth: Float) {
        for (i in 1 until anglesCount) {
            if (posX >= CPUX - (pWidth / anglesCount) * (anglesCount + 1 - i) && posX <= CPUX - (pWidth / anglesCount) * (anglesCount - i))
                dirX = -(anglesCount - i) / 10f
            else if (posX <= CPUX + (pWidth / anglesCount) * (anglesCount + 1 - i) && posX >= CPUX + (pWidth / anglesCount) * (anglesCount - i)) {
                dirX = (anglesCount - i) / 10f
            }
        }
        dirY = -1 * dirY / Math.abs(dirY) * Math.sqrt((1 - dirX * dirX).toDouble()).toFloat()
        playSound()
        //dirY = -1 * dirY / Math.abs(dirY) * Math.sqrt((1 - dirX * dirX).toDouble()).toFloat()
        dirY = Math.sqrt((1 - dirX * dirX).toDouble()).toFloat()

        if (speed < maxSpeed) {
            speed += 0.1f
        }
    }

    fun playSound(){
        pongSoung.start()
    }
}
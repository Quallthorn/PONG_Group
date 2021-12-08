package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameSounds
import kotlin.math.sqrt
import com.example.pong_group.Services.NumberPrinter


class GameViewPONG(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    private var player: Paddle
    private var cpu: Paddle
    private var paddlePosY = 0f
    private var ball1: Ball
    private var ballA = mutableListOf<Ball>()
    private val ballCount = 0
    var mHolder: SurfaceHolder? = holder
//    var screenWidth: Float = 0f
//    var screenHeight: Float = 0f

    val numberFromEdge = 100f
    val numberFromMiddle = 100f


    companion object {
        var canvas: Canvas = Canvas()
    }

    init {
        mHolder?.addCallback(this)

        player = Paddle(false)
        cpu = Paddle(true)
        ball1 = Ball()
        changeColors()
    }

    fun setup() {
        player.scorePositionXL = GameSettings.screenWidth - numberFromEdge - NumberPrinter.numberWL
        player.scorePositionXR =
            GameSettings.screenWidth - numberFromEdge - NumberPrinter.numberWL * 2 - NumberPrinter.numberW
        player.scorePositionY = GameSettings.screenHeight / 2 + numberFromMiddle
        cpu.scorePositionXL = numberFromEdge
        cpu.scorePositionXR = numberFromEdge + NumberPrinter.numberWL + NumberPrinter.numberW
        cpu.scorePositionY =
            GameSettings.screenHeight / 2 - numberFromMiddle - NumberPrinter.numberH

        paddlePosY = GameSettings.screenHeight / 7.2f
        player.posY = paddlePosY
        cpu.posY = GameSettings.screenHeight - paddlePosY

        ball1.centerBall()
        ball1.start = true
        for (i in 0 until ballCount) {
            var newBall = Ball()
            var s = (0..100).random() / 100f
            newBall.dirX = s
            newBall.dirY = sqrt((1 - newBall.dirX * newBall.dirX).toDouble()).toFloat()
            //var d = 2
            when ((0..3).random()) {
                1 -> newBall.dirX = newBall.dirX * -1
                2 -> newBall.dirY = newBall.dirY * -1
                3 -> {
                    newBall.dirY = newBall.dirY * -1
                    newBall.dirX = newBall.dirX * -1
                }
                else -> {}
            }
            newBall.paint.color = GameSettings.getRandomColorFromArray()
            ballA.add(newBall)
        }
    }

    fun start() {
        running = true
        thread = Thread(this)
        thread?.start()
    }

    fun stop() {
        running = false
        try {
            thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun update() {
        player.update()

        if (ball1.start) {
            if (cpu.posX <= ball1.posX - cpu.width / 4)
                cpu.posX += ball1.speed / (1..2).random()
            else if (cpu.posX >= ball1.posX + cpu.width / 4)
                cpu.posX -= ball1.speed / (1..2).random()
        } else {
            if (cpu.posX < GameSettings.screenWidth / 2)
                cpu.posX += GameSettings.screenWidth / 250f
            if (cpu.posX > GameSettings.screenWidth / 2)
                cpu.posX -= GameSettings.screenWidth / 250f

            if (!ball1.p1Scored
                && Math.abs(cpu.posX - GameSettings.screenWidth / 2) <= 5f
                && Math.abs(ball1.posX - GameSettings.screenWidth / 2) <= 5f
            ){
                GameSounds.playSound()
                ball1.start = true
            }
        }

        ball1.update(
            player.posX,
            player.posY,
            player.width,
            player.posXOld,
            cpu.posX
        )
        ballA.forEach {
            it.update(
                player.posX,
                player.posY,
                player.width,
                player.posXOld,
                cpu.posX
            )
        }
        if (ball1.changeColor)
            changeColors()
    }

    private fun draw() {
        canvas = mHolder!!.lockCanvas()
        canvas.drawColor(Color.BLACK)
        drawLine()
        player.draw()
        cpu.draw()
        ball1.draw(canvas)
        ballA.forEach {
            it.draw(canvas)
        }
        mHolder!!.unlockCanvasAndPost(canvas)
    }

    fun drawLine() {
        var lineX = 0f
        val amount = 20
        val lineSpacing = 30
        val lineW = (GameSettings.screenWidth + lineSpacing) / amount - lineSpacing
        val thickness = 5f
        for (i in 0 until amount) {
            canvas?.drawRect(
                lineX,
                GameSettings.screenHeight / 2 - thickness,
                lineX + lineW,
                GameSettings.screenHeight / 2 + thickness,
                GameSettings.curPaint
            )
            lineX += (lineW + lineSpacing)
        }
    }

    private fun changeColors() {
        GameSettings.getRandomColorFromArray()
        player.paint = GameSettings.curPaint
        cpu.paint = GameSettings.curPaint
        ball1.paint = GameSettings.curPaint
        ball1.changeColor = false
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        GameSettings.setScreenDimen(p2.toFloat(), p3.toFloat())
        setup()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        stop()
    }

    override fun run() {
        while (running) {
            update()
            draw()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            player.posX = event.x
            if (ball1.p1Scored && !ball1.start) {
                if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL){
                    GameSounds.playSound()
                    ball1.start = true
                }
            }
        }
        return true
    }
}

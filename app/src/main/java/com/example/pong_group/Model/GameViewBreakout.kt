package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings

class GameViewBreakout(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    lateinit var canvas: Canvas
    private var player: Paddle
    private val playerY = 250f
    private var ball1: BallBreakout
    private var ballA = mutableListOf<BallBreakout>()
    private var bricks = mutableListOf<Brick>()
    var rngColor = Paint()
    val ballCount = 0
    var mHolder: SurfaceHolder? = holder

    var gridPosX: Float = 0f
    var gridPosY: Float = 0f
    var gridStartX: Float = 0f
    var gridStartY: Float = 120f
    var gridSpacingX: Float = 0f
    var gridSpacingY: Float = 0f
    var brickH: Float = 50f
    var brickW: Float = 0f
    var brickCountX: Int = 20
    var brickCountY: Int = 6
    var randomChance: Int = 0

    init {
        mHolder?.addCallback(this)

        gridPosX = gridStartX
        gridPosY = gridStartY

        player = Paddle(false)
        player.posY = playerY

        ball1 = BallBreakout()
        changeColors()
    }

    fun setup() {
        ball1.centerBall(player.posX, player.posY)
        for (i in 0 until ballCount) {
            var newBall = BallBreakout()
            var s = (0..100).random() / 100f
            newBall.dirX = s
            newBall.dirY = Math.sqrt((1 - newBall.dirX * newBall.dirX).toDouble()).toFloat()
            var d = (0..3).random()
            //var d = 2
            when (d) {
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

        brickW = ((GameSettings.screenWidth - gridStartX * 2 + gridSpacingX) / brickCountX) - gridSpacingX
        var rowNumber = 0
        for (i in 0 until (brickCountX * brickCountY)) {
            var newBrick = Brick(brickW, brickH, gridPosX, gridPosY)
            getBrickColor(rowNumber, newBrick)
            gridPosX += (gridSpacingX + brickW)
            if (gridPosX >= (gridSpacingX + brickW) * brickCountX) {
                gridPosX = gridStartX
                gridPosY += (gridSpacingY + brickH)
                rowNumber++
            }
            bricks.add(newBrick)
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

    fun update() {
        player.update()
        ball1.update(
            player.posX,
            player.posY,
            player.width,
            player.posXOld,
        )
        ballA.forEach {
            it.update(player.posX, player.posY, player.width, player.posXOld)
        }
        bricks.forEach {
            it.update(ball1)
        }
        if (ball1.changeColor)
            changeColors()
    }

    fun draw() {
        canvas = mHolder!!.lockCanvas()
        canvas.drawColor(Color.BLACK)
        player.draw()
        ball1.draw(canvas)
        ballA.forEach {
            it.draw(canvas)
        }
        bricks.forEach {
            it.draw(canvas)
        }
        mHolder!!.unlockCanvasAndPost(canvas)
    }

    fun changeColors() {
        rngColor.color = GameSettings.getRandomColorFromArray()
        player.paint = rngColor
        ball1.paint = rngColor
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
        if (event != null && event.x >= player.width / 2 && event.x <= GameSettings.screenWidth - player.width / 2) {
            player.posX = event.x
        }
        return true
    }

    fun getBrickColor(rowNumber: Int, newBrick: Brick){
        when (rowNumber) {
            0 -> {
                newBrick.paint.color = context.resources.getColor(R.color.red)
                true
            }
            1 -> {
                newBrick.paint.color = context.resources.getColor(R.color.orange)
                true
            }
            2 -> {
                newBrick.paint.color = context.resources.getColor(R.color.pale_orange)
                true
            }
            3 -> {
                newBrick.paint.color = context.resources.getColor(R.color.yellow)
                true
            }
            4 -> {
                newBrick.paint.color = context.resources.getColor(R.color.green)
                true
            }
            5 -> {
                newBrick.paint.color = context.resources.getColor(R.color.blue)
                true
            }
            else -> {newBrick.paint.color = context.resources.getColor(R.color.white)}
        }
    }
}

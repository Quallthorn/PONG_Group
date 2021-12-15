package com.example.pong_group.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.pong_group.Controller.App
import android.view.View
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameThread

import android.widget.TextView

import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat


class GameViewBreakout(context: Context) : SurfaceView(context), SurfaceHolder.Callback{

    private val thread: GameThread
    private var player: PaddleBreakout
    private var paddlePosY = 0f
    private var ball: BallBreakout
    private var bricks = mutableListOf<Brick>()
    private var rngColor = Paint()

    private val colorArray = App.instance.resources.obtainTypedArray(R.array.breakout_bricks)
    var level = 2
    var gridPosX: Float = 0f
    var gridPosY: Float = 0f
    var gridStartX: Float = 0f
    var gridStartY: Float = 120f
    var gridSpacingX: Float = 0f
    var gridSpacingY: Float = 0f
    var brickH: Float = 50f
    var brickW: Float = 0f
    private val brickCountX: Int = 15
    private val brickCountY: Int = (when (level) {
        1 -> {
            6
        }
        2 -> {
            9
        }
        else -> {
            0
        }
    })

    companion object {
        var canvasBreakout = Canvas()
        var totalCountOfBricks = 0
    }

    init {
        holder.addCallback(this)

        gridPosX = gridStartX
        gridPosY = gridStartY

        player = PaddleBreakout()
        ball = BallBreakout()
        changeColors()
        totalCountOfBricks = brickCountX*brickCountY
        thread = GameThread(holder, this)
    }

    private fun setup() {
        paddlePosY = GameSettings.screenHeight / 7.2f
        player.posY = paddlePosY
        ball.centerBall(player.posX, player.posY)

        brickW = ((GameSettings.screenWidth - gridStartX * 2 + gridSpacingX) / brickCountX) - gridSpacingX
        var rowNumber = 1
        if (level == 2)
            rowNumber = 0

        for (i in 0 until (brickCountX * brickCountY)) {
            //set position
            var newBrick = Brick(brickW, brickH, gridPosX, gridPosY)

            //set color
            val colorInt = colorArray.getColor(rowNumber, 0)
            newBrick.paint.color = colorInt

            //change position for next brick
            gridPosX += (gridSpacingX + brickW)
            if (gridPosX >= (gridSpacingX + brickW) * brickCountX) {
                gridPosX = gridStartX
                gridPosY += (gridSpacingY + brickH)
                rowNumber++
            }
            bricks.add(newBrick)
        }
    }

    fun update() {
        //player.update()
        ball.update(
            player.posX,
            player.posY,
            player.width
        )
        bricks.forEach {
            it.update(ball)
        }
        if (ball.changeColor)
            changeColors()
    }

    override  fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.also {
            it.drawColor(Color.BLACK)
            player.draw()
            ball.draw()
            bricks.forEach { brick ->
                brick.draw()
            }
            checkEndOfTheGame()
        }
    }

    private fun changeColors() {
        rngColor.color = GameSettings.getRandomColorFromArray()
        player.paint = rngColor
        ball.paint = rngColor
        ball.changeColor = false
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        thread.running = true
        thread.start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        GameSettings.setScreenDimen(p2.toFloat(), p3.toFloat())
        setup()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        thread.running = false
        thread.join()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            player.posX = event.x
        }
        return true
    }

    fun checkEndOfTheGame(){
        if(totalCountOfBricks == 0) {
            val layout = LinearLayout(App.instance)

            val textView = TextView(App.instance)
            textView.visibility = View.VISIBLE
            textView.text = "GAME \n OVER"
            textView.textSize = 100f
            val typeFace = ResourcesCompat.getFont(App.instance, R.font.arcade_classic)
            textView.typeface = typeFace
            textView.setTextColor(App.instance.resources.getColor(R.color.white))
            layout.addView(textView)

            layout.measure(canvasBreakout.width, canvasBreakout.height)
            layout.layout(0, 0, canvasBreakout.width, canvasBreakout.height)


            canvasBreakout.translate((canvasBreakout.width/2).toFloat()-(textView.width/2).toFloat(), (canvasBreakout.width/2).toFloat())

            layout.draw(canvasBreakout)
            thread.running = false

        }
    }
}
//for multiple balls (power up?)
//private var ballA = mutableListOf<BallBreakout>()
//val ballCount = 0

//        for (i in 0 until ballCount) {
//            var newBall = BallBreakout()
//            var s = (0..100).random() / 100f
//            newBall.dirX = s
//            newBall.dirY = Math.sqrt((1 - newBall.dirX * newBall.dirX).toDouble()).toFloat()
//            var d = (0..3).random()
//            when (d) {
//                1 -> newBall.dirX = newBall.dirX * -1
//                2 -> newBall.dirY = newBall.dirY * -1
//                3 -> {
//                    newBall.dirY = newBall.dirY * -1
//                    newBall.dirX = newBall.dirX * -1
//                }
//                else -> {}
//            }
//            newBall.paint.color = GameSettings.getRandomColorFromArray()
//            ballA.add(newBall)
//        }

// in update ()
//        ballA.forEach {
//            it.update(player.posX, player.posY, player.width, player.posXOld)
//        }

// in draw()
//        ballA.forEach {
//            it.draw(canvas)
//        }

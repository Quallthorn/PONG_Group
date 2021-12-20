package com.example.pong_group.Model

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.pong_group.Controller.App
import android.view.View
import android.widget.Button
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameThread

import android.widget.TextView

import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat


class GameViewBreakout(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private val thread: GameThread
    private var player: PaddleBreakout
    private var paddlePosY = 0f
    private var ball: BallBreakout
    private var bricks = mutableListOf<Brick>()
    private var rngColor = Paint()

    lateinit var restartButton: Button

    private val colorArray: TypedArray
    private val classic = GameSettings.classicBreakout
    var everyOther = false
    var level = 1
    var gridPosX: Float = 0f
    var gridPosY: Float = 0f
    var gridSqueezeX: Float = 0f
    var gridStartY: Float = 120f
    var gridSpacingX: Float = 0f
    var gridSpacingY: Float = 0f
    var brickH: Float = 50f
    var brickW: Float = 0f
    private var brickCountX: Int = 15
    private var brickCountY: Int

    companion object {
        var canvasBreakout = Canvas()
        var totalCountOfBricks = 0
    }

    init {
        if (classic) {
            brickCountX = 14
            brickCountY = 8
            gridSpacingX = 5f
            gridSpacingY = 5f
            brickH = 20f
            colorArray = App.instance.resources.obtainTypedArray(R.array.breakout_bricks_classic)
        } else {
            colorArray = App.instance.resources.obtainTypedArray(R.array.breakout_bricks)
            brickCountY = (when (level) {
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
        }
        holder.addCallback(this)

        gridPosX = gridSqueezeX
        gridPosY = gridStartY

        player = PaddleBreakout()
        ball = BallBreakout()
        changeColors()
        totalCountOfBricks = brickCountX * brickCountY
        thread = GameThread(holder, this)

        GameSettings.gameSetUpBreakout(brickCountX, brickCountY)

        setupButton()
        restartButton.setOnClickListener {
            Log.d("Button", "button pressed")
        }
    }


    private fun setup() {
        paddlePosY = GameSettings.screenHeight / 7.2f
        player.posY = paddlePosY
        ball.centerBall(player.posX, player.posY)

        brickW =
            ((GameSettings.screenWidth - gridSqueezeX * 2) / brickCountX) - gridSpacingX
        var colorNumber = 1
        var pointBase = brickCountY
        if (level == 2 || classic)
            colorNumber = 0
        if (classic) {
            pointBase = 7
        }

        for (i in 0 until (brickCountX * brickCountY)) {
            //set position
            var newBrick = Brick(brickW, brickH, gridPosX, gridPosY, pointBase)

            //set color
            val colorInt = colorArray.getColor(colorNumber, 0)
            newBrick.paint.color = colorInt

            //change position for next brick
            gridPosX += (gridSpacingX + brickW)
            if (gridPosX >= (gridSpacingX + brickW) * brickCountX + gridSqueezeX) {
                gridPosX = gridSqueezeX
                gridPosY += (gridSpacingY + brickH)
                colorNumber++
                if (classic){
                    everyOther = if (everyOther){
                        pointBase -= 2
                        false
                    } else
                        true
                }
                else
                    pointBase--
            }
            bricks.add(newBrick)
        }
    }

    fun update() {
        //player.update()
        ball.update(player)
        bricks.forEach {
            it.update(ball)
        }
        if (ball.changeColor)
            changeColors()
    }

    override fun draw(canvas: Canvas) {
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
        if (!classic){
            rngColor.color = GameSettings.getRandomColorFromArray()
            player.paint = rngColor
            ball.paint = rngColor
            ball.changeColor = false
        }
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


    fun checkEndOfTheGame() {
        if (totalCountOfBricks == 0) {
            val layout = LinearLayout(App.instance)
            layout.orientation = LinearLayout.VERTICAL

            val GameOverTextView = TextView(App.instance)
            GameOverTextView.visibility = View.VISIBLE
            GameOverTextView.text = "GAME \n OVER"
            GameOverTextView.textSize = 100f
            val typeFace = ResourcesCompat.getFont(App.instance, R.font.arcade_classic)
            GameOverTextView.typeface = typeFace
            GameOverTextView.setTextColor(App.instance.resources.getColor(R.color.white))

            layout.addView(GameOverTextView)
            layout.addView(restartButton)


            layout.measure(canvasBreakout.width, canvasBreakout.height)
            layout.layout(0, 0, canvasBreakout.width, canvasBreakout.height)


            canvasBreakout.translate(
                (canvasBreakout.width / 2).toFloat() - (GameOverTextView.width / 2).toFloat(),
                (canvasBreakout.width / 2).toFloat()
            )


            layout.draw(canvasBreakout)
            thread.running = false

        }
    }

    fun setupButton() {
        restartButton = Button(App.instance)
        restartButton.text = "Restart"
        val typeFace = ResourcesCompat.getFont(App.instance, R.font.arcade_classic)
        restartButton.typeface = typeFace
        restartButton.textSize = 40f
        restartButton.setTextColor(App.instance.resources.getColor(R.color.white))
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

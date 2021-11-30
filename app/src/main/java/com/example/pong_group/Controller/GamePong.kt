package com.example.pong_group.Controller

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import com.example.pong_group.R
import com.example.pong_group.databinding.ActivityGamePongBinding

class GamePong : AppCompatActivity(), SurfaceHolder.Callback, View.OnTouchListener {

    private var ballPosX: Float = 5f
    private var ballPosY: Float = 5f
    private var ballRadius: Float = 5f
    private var paddleX: Float = 1f
    private var paddleY: Float = 250f
    private var paddleH: Float = 10f
    private var paddleW: Float = 80f

    private var gridPosX: Float = 0f
    private var gridPosY: Float = 0f
    private var gridStartX: Float = 10f
    private var gridStartY: Float = 120f
    private var gridSpacingX: Float = 15f
    private var gridSpacingY: Float = 25f
    private var boxH: Float = 50f
    private var boxW: Float = 0f
    private var boxCountX: Int = 10
    private var boxCountY: Int = 6
    private var randomChance: Int = 0

    private val surfaceBackground = Paint()
    private var ballPaint: Paint = Paint()
    private var boxPaint: Paint = Paint()

    private var move: Boolean = false
    private var ballSpeed: Float = 20f
    private var ballDirX: Int = 1
    private var ballDirY: Int = 1

    private val context: Context = this
    private var initialize: Boolean = true

    private lateinit var binder: ActivityGamePongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_pong)
        binder = ActivityGamePongBinding.inflate(layoutInflater)
        setContentView(binder.root)

        binder.surfaceViewPong.setOnTouchListener(this)
        binder.surfaceViewPong.holder.addCallback(this)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_O -> {
                move = true
                fixedUpdate()
                true
            }
            KeyEvent.KEYCODE_M -> {
                drawGrid()
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }

    private fun drawBall() {
        val canvas: Canvas? = binder.surfaceViewPong.holder.lockCanvas()
        surfaceBackground.color = Color.BLACK
        ballPaint.color = Color.WHITE

        //Background
        canvas?.drawRect(
            0f,
            0f,
            binder.surfaceViewPong.width.toFloat(),
            binder.surfaceViewPong.height.toFloat(),
            surfaceBackground
        )

        //Paddle
        canvas?.drawRect(
            paddleX - paddleW,
            binder.surfaceViewPong.height.toFloat() - paddleY,
            paddleX + paddleW,
            binder.surfaceViewPong.height.toFloat() - paddleY + paddleH,
            ballPaint
        )

        //Ball
        canvas?.drawCircle(ballPosX, ballPosY, 5f, ballPaint)

        binder.surfaceViewPong.holder.unlockCanvasAndPost(canvas)
        binder.surfaceViewPong.setZOrderOnTop(true)
    }

    private fun moveBall() {
        if (this.ballPosX >= binder.surfaceViewPong.width.toFloat() - ballRadius)
            this.ballDirX = -1
        else if (this.ballPosX <= ballRadius)
            this.ballDirX = 1

        if (this.ballPosY >= binder.surfaceViewPong.height.toFloat() - ballRadius)
            ballDirY = -1
        else if (this.ballPosY <= ballRadius)
            ballDirY = 1

        //Portal?
//            if (this.ballPosY >= binder.surfaceViewPong.height.toFloat() - paddleY - ballRadius
//                && this.ballPosY <= binder.surfaceViewPong.height.toFloat() - paddleY + paddleH
//                && this.ballPosX <= this.paddleX - paddleW
//                || this.ballPosY >= binder.surfaceViewPong.height.toFloat() - paddleY - ballRadius
//                && this.ballPosY <= binder.surfaceViewPong.height.toFloat() - paddleY + paddleH
//                && this.ballPosX >= this.paddleX + paddleW)
//                ballDirY = ballDirY * -1

        if (this.ballPosY >= binder.surfaceViewPong.height.toFloat() - paddleY - ballRadius
            && this.ballPosY <= binder.surfaceViewPong.height.toFloat() - paddleY + paddleH
            && this.ballPosX >= this.paddleX - paddleW
            && this.ballPosX <= this.paddleX + paddleW
        )
            ballDirY = ballDirY * -1

        this.ballPosX += ballDirX * ballSpeed
        this.ballPosY += ballDirY * ballSpeed
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        if (initialize) {
            val canvas: Canvas? = binder.surfaceViewPong.holder.lockCanvas()
            ballPosX = binder.surfaceViewPong.width.toFloat() * 0.5f
            ballPosY = binder.surfaceViewPong.height.toFloat() * 0.5f
            binder.surfaceViewPong.holder.unlockCanvasAndPost(canvas)
            initialize = false
        }
        if (p0 is SurfaceView) {
            val x = p1?.x
            if (x != null) {
                this.paddleX = x
            }
            moveBall()
            drawBall()
            return true
        } else {
            return false
        }
    }

    private fun fixedUpdate() {
        if (move) {
            moveBall()
            drawBall()
            fixedUpdate()
        }
    }

    private fun drawGrid() {
        val canvas: Canvas? = binder.surfaceViewPong.holder.lockCanvas()
        surfaceBackground.color = Color.BLACK
        ballPaint.color = Color.WHITE

        //Background
        canvas?.drawRect(
            0f,
            0f,
            binder.surfaceViewPong.width.toFloat(),
            binder.surfaceViewPong.height.toFloat(),
            surfaceBackground
        )

        boxPaint.color = context.resources.getColor(R.color.red)
        gridPosX = gridStartX
        gridPosY = gridStartY
        boxW = ((binder.surfaceViewPong.width.toFloat() - gridStartX * 2 + gridSpacingX) / boxCountX) - gridSpacingX
        var rowNumber: Int = 0
        var drawBool: Boolean = true
        var boxCountTotal: Int = 0
        var grid: IntArray = IntArray(boxCountX * boxCountY)
        for (i in 0 until boxCountX * boxCountY) {
            grid[i] = (0..randomChance).random()
        }
        while (rowNumber < boxCountY) {
            while (drawBool) {
                if (grid[boxCountTotal] == 0) {
                    canvas?.drawRect(
                        gridPosX,
                        gridPosY,
                        gridPosX + boxW,
                        gridPosY + boxH,
                        boxPaint
                    )
                }
                gridPosX += (gridSpacingX + boxW)
                boxCountTotal++
                if (gridPosX >= (gridSpacingX + boxW) * boxCountX) {
                    gridPosX = gridStartX
                    gridPosY += (gridSpacingY + boxH)
                    rowNumber++
                    when (rowNumber) {
                        1 -> {
                            boxPaint.color = context.resources.getColor(R.color.orange)
                            true
                        }
                        2 -> {
                            boxPaint.color = context.resources.getColor(R.color.pale_orange)
                            true
                        }
                        3 -> {
                            boxPaint.color = context.resources.getColor(R.color.yellow)
                            true
                        }
                        4 -> {
                            boxPaint.color = context.resources.getColor(R.color.green)
                            true
                        }
                        5 -> {
                            boxPaint.color = context.resources.getColor(R.color.blue)
                            true
                        }
                        else -> boxPaint.color = context.resources.getColor(R.color.white)
                    }
                    drawBool = false
                }
            }
            drawBool = true
        }
        binder.surfaceViewPong.holder.unlockCanvasAndPost(canvas)
        binder.surfaceViewPong.setZOrderOnTop(true)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {

    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {

    }
}
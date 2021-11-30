package com.example.pong_group.Controller

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import com.example.pong_group.R
import com.example.pong_group.databinding.ActivityGamePongBinding

class GamePong : AppCompatActivity(), SurfaceHolder.Callback, View.OnTouchListener {

    private var ballPosX: Float = 100f
    private var ballPosY: Float = 5f
    private var ballRadius: Float = 5f
    private var paddleX: Float = 1f
    private var paddleY: Float = 250f
    private var paddleH: Float = 10f
    private var paddleW: Float = 80f

    private val surfaceBackground = Paint()
    private var ballPaint: Paint = Paint()

    private var move: Boolean = false
    private var ballSpeed: Float = 20f
    private var ballDirX: Int = 1
    private var ballDirY: Int = 1

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
        val canvas: Canvas? = binder.surfaceViewPong.holder.lockCanvas()
        ballPaint.color = Color.WHITE

        canvas?.drawCircle(ballPosX, ballPosY, ballRadius, ballPaint)

        binder.surfaceViewPong.holder.unlockCanvasAndPost(canvas)
        binder.surfaceViewPong.setZOrderOnTop(true)
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        if (p0 is SurfaceView) {
            val x = p1?.x

            if (x != null) {
                this.paddleX = x
            }

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
                && this.ballPosX <= this.paddleX + paddleW)
                ballDirY = ballDirY * -1

            this.ballPosX += ballDirX * ballSpeed
            this.ballPosY += ballDirY * ballSpeed

            drawBall()
            return true
        } else {
            return false
        }
    }

    private fun fixedUpdate() {
        while (move) {
            if (ballPosX >= binder.surfaceViewPong.width.toFloat() - ballRadius)
                ballDirX = -1
            else if (ballPosX <= ballRadius)
                ballDirX = 1

            if (ballPosY >= binder.surfaceViewPong.height.toFloat() - ballRadius)
                ballDirY = -1
            else if (ballPosY <= ballRadius)
                ballDirY = 1

            ballPosX += ballDirX * ballSpeed
            ballPosY += ballDirY * ballSpeed
            drawBall()
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder) {

    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {

    }
}
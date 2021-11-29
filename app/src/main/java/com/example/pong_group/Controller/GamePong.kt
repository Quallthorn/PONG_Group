package com.example.pong_group.Controller

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.example.pong_group.R
import com.example.pong_group.databinding.ActivityGamePongBinding

class GamePong : AppCompatActivity(), SurfaceHolder.Callback, View.OnTouchListener {

    var circleX: Float = 1f
    var circleY: Float = 250f
    var ballPaint: Paint = Paint()
    var paddleX: Float = 1f

    private lateinit var binder: ActivityGamePongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_pong)
        binder = ActivityGamePongBinding.inflate(layoutInflater)
        setContentView(binder.root)

        binder.surfaceViewPong.setOnTouchListener(this)

        binder.surfaceViewPong.holder.addCallback(this)
    }

    private fun drawBall() {
        val canvas: Canvas? = binder.surfaceViewPong.holder.lockCanvas()
        val surfaceBackground = Paint()
        surfaceBackground.color = Color.BLACK

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
            paddleX - 100f,
            binder.surfaceViewPong.height.toFloat() - 250f,
            paddleX + 100f,
            binder.surfaceViewPong.height.toFloat() - 200f,
            ballPaint
        )

        ballPaint.color = Color.WHITE
//        canvas?.drawCircle(circleX, circleY, 50f, ballPaint)
        binder.surfaceViewPong.holder.unlockCanvasAndPost(canvas)

        binder.surfaceViewPong.setZOrderOnTop(true)
    }

    private fun moveBall(){

    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        if (p0 is SurfaceView) {
            val x = p1?.x
            //val y = p1?.y

            if (x != null) {
                //this.circleX = x
                this.paddleX = x
            }
//            if (y != null) {
//                this.circleY = y
//            }

            drawBall()

            return true
        } else {
            return false
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder) {

    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {

    }
}
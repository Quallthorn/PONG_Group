package com.example.pong_group.Model

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Build
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.example.pong_group.Controller.App
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameThread

import android.widget.TextView

import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.example.pong_group.Services.SharedBreakout


class GameViewBreakout(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private var thread: GameThread
    private var player: PaddleBreakout
    private var paddlePosY = 0f
    private var ball: BallBreakout
    private var rngColor = Paint()

    private lateinit var restartButton: SurfaceViewButton
    var pause = false

    private val colorArray: TypedArray
    private val classic = GameSettings.classicBreakout
    private val typeFace = ResourcesCompat.getFont(App.instance, R.font.arcade_classic)
    private var everyOther = false
    var level = 1
    var gridPosX: Float = 0f
    var gridPosY: Float = 0f
    var gridSqueezeX: Float = 0f
    var gridStartY: Float = 120f
    var gridSpacingX: Float = 0f
    var gridSpacingY: Float = 0f
    var brickH: Float = 50f
    var brickW: Float = 0f
    var isButtonClickable = false

    companion object {
        var canvasBreakout = Canvas()
        var totalCountOfBricks = 0

        var outOfLives = false
        var lives = 3
        var breakReady = true
    }

    init {
        if (classic) {
            SharedBreakout.brickCountX = 14
            SharedBreakout.brickCountY = 8
            gridSpacingX = 5f
            gridSpacingY = 5f
            brickH = 20f
            colorArray = App.instance.resources.obtainTypedArray(R.array.breakout_bricks_classic)
        } else {
            colorArray = App.instance.resources.obtainTypedArray(R.array.breakout_bricks)
            SharedBreakout.brickCountY = 6
        }
        holder.addCallback(this)

        gridPosX = gridSqueezeX
        gridPosY = gridStartY

        player = PaddleBreakout()
        ball = BallBreakout()
        changeColors()

        thread = GameThread(holder, this)

        SharedBreakout.gameSetUpBreakout()
    }



    private fun setup() {
        gridPosX = gridSqueezeX
        gridPosY = gridStartY
        totalCountOfBricks = SharedBreakout.brickCountX*SharedBreakout.brickCountY
        paddlePosY = GameSettings.screenHeight / 7.2f
        player.posY = paddlePosY
        ball.centerBall(player.posX, player.posY)

        brickW =
            ((GameSettings.screenWidth - gridSqueezeX * 2) / SharedBreakout.brickCountX) - gridSpacingX
        var colorNumber = 1
        var pointBase = SharedBreakout.brickCountY
        if (level == 2 || classic)
            colorNumber = 0
        if (classic) {
            pointBase = 7
        }

        outOfLives = false
        SharedBreakout.bricks.clear()

        for (i in 0 until (SharedBreakout.brickCountX * SharedBreakout.brickCountY)) {
            //set position
            val newBrick = Brick(brickW, brickH, gridPosX, gridPosY, pointBase, i)

            //set color
            val colorInt = colorArray.getColor(colorNumber, 0)
            newBrick.paint.color = colorInt

            //change position for next brick
            gridPosX += (gridSpacingX + brickW)
            if (gridPosX >= (gridSpacingX + brickW) * SharedBreakout.brickCountX + gridSqueezeX) {
                gridPosX = gridSqueezeX
                gridPosY += (gridSpacingY + brickH)
                colorNumber++

                //set score for brick
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
            SharedBreakout.bricks.add(newBrick)
        }
    }

    fun update() {
        //player.update()
        ball.update(player)
        SharedBreakout.bricks.forEach {
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
            if (!outOfLives)
                ball.draw()
            SharedBreakout.bricks.forEach { brick ->
                brick.draw()
            }
            scoresAndLivesCounter()
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
        if (thread.state == Thread.State.TERMINATED) {
            thread = GameThread(holder, this)
        }
        thread.running = true
        thread.start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        GameSettings.setScreenDimen(p2.toFloat(), p3.toFloat())
        setup()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        while (thread.isAlive) {
            try {
                thread.running = false
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            player.posX = event.x
        }

        val x = event?.x
        val y = event?.y

        if (isButtonClickable) {
            if (restartButton.btn_rect!!.contains(x!!,y!!)){
                isButtonClickable = false
                if(level == 1 && !outOfLives){
                    level = 2
                    if (!classic){
                        SharedBreakout.brickCountY = 9
                    }
                    else{
                        SharedBreakout.brickCountY = 8
                    }
                } else {
                    lives = 3
                    GameSettings.scoreBreakout = 0
                    level = 1
                    if (!classic)
                        SharedBreakout.brickCountY = 6
                    else
                        SharedBreakout.brickCountY = 8
                }
                setup()
                resumeThread()
            }
        }
        return true
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun checkEndOfTheGame() {
            //game over layout
            if (totalCountOfBricks == 0 || outOfLives) {
                val layout = LinearLayout(App.instance)
                layout.orientation = LinearLayout.VERTICAL
                layout.gravity = Gravity.CENTER

                var gameOverTextView = TextView(App.instance)

                val textParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1F)

                gameOverTextView.visibility = View.VISIBLE
                when {
                    outOfLives -> {
                        gameOverTextView.text = "YOU\nLOSE"
                    }
                    level == 1 -> {
                        gameOverTextView.text = "NEXT\nLEVEL"
                    }
                    else -> {
                        gameOverTextView.text = "GAME\nOVER"
                    }
                }
                gameOverTextView.textSize = 100f
                gameOverTextView.setLineSpacing(0f, 0.7f)
                gameOverTextView.gravity = Gravity.CENTER
                gameOverTextView.typeface = typeFace
                gameOverTextView.layoutParams = textParams
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    gameOverTextView.setTextColor(
                        App.instance.resources.getColor(
                            R.color.white,
                            context.theme
                        )
                    )
                } else {
                    gameOverTextView.setTextColor(App.instance.resources.getColor(R.color.white))
                }

                gameOverTextView.measure(0, 0)
                val gameTextViewHeight = gameOverTextView.measuredHeight

                layout.addView(gameOverTextView)

                layout.measure(canvasBreakout.width, canvasBreakout.height)
                layout.layout(0, 0, canvasBreakout.width, canvasBreakout.height)
                layout.draw(canvasBreakout)

                //restart button layout

                if (level == 1 && !outOfLives) {
                    val icon = BitmapFactory.decodeResource(App.instance.resources, R.drawable.play)
                    setupButton(icon, gameTextViewHeight)
                } else {
                    val icon = BitmapFactory.decodeResource(App.instance.resources, R.drawable.reset)
                    setupButton(icon, gameTextViewHeight)
                }
                isButtonClickable = true
                pauseThread()
            }

        }

    fun setupButton(icon: Bitmap, textViewHeight: Int){
        restartButton = SurfaceViewButton(icon)
        val restartButtonX = (canvasBreakout.width / 2).toFloat() - restartButton.width / 2
        val restartButtonY = (canvasBreakout.height / 2).toFloat() + textViewHeight/2 + 40f
        restartButton.setPosition(restartButtonX, restartButtonY)
        restartButton.draw(canvasBreakout)
    }

    private fun scoresAndLivesCounter() {
        val livesLayout = LinearLayout(App.instance)
        livesLayout.orientation = LinearLayout.HORIZONTAL
//        var params = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
//        livesLayout.layoutParams = params


        val textParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, 1F)
        val livesText = TextView(App.instance)
        livesText.text = "Lives: $lives"
        livesText.textSize = 24F
        livesText.gravity = Gravity.START
        livesText.typeface = typeFace
        livesText.layoutParams = textParams
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            livesText.setTextColor(App.instance.resources.getColor(R.color.white, context.theme))
        }

        val levelText = TextView(App.instance)
        levelText.text = "Lv: $level"
        levelText.textSize = 24F
        levelText.gravity = Gravity.CENTER_HORIZONTAL
        levelText.typeface = typeFace
        levelText.layoutParams = textParams
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            levelText.setTextColor(App.instance.resources.getColor(R.color.white, context.theme))
        }

        val scoresText = TextView(App.instance)
        scoresText.text = "${GameSettings.scoreBreakout}"
        scoresText.textSize = 24F
        scoresText.gravity = Gravity.END
        scoresText.typeface = typeFace
        livesText.layoutParams = textParams
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scoresText.setTextColor(App.instance.resources.getColor(R.color.white, context.theme))
        }

//        var paramsLevel =  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
//        paramsLevel.addRule(RelativeLayout.ALIGN_BOTTOM, RelativeLayout.TRUE)
//
//        val levelText = TextView(App.instance)
//        levelText.layoutParams = paramsLevel
//        levelText.text = "Level: $level"
//        levelText.textSize = 36f
//        levelText.gravity = Gravity.BOTTOM
//        levelText.typeface = typeFace
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            levelText.setTextColor(App.instance.resources.getColor(R.color.white, context.theme))
//        }

//        levelText.x = (canvasBreakout.width/2 - levelText.width/2).toFloat()
//        levelText.y = canvasBreakout.height/2 - levelText.height -10f
//
//        levelText.measure(canvasBreakout.width, canvasBreakout.height)
//        levelText.layout(0, 0, canvasBreakout.width, canvasBreakout.height)
//
//        levelText.draw(canvasBreakout)

        livesLayout.addView(livesText)
        livesLayout.measure(canvasBreakout.width, canvasBreakout.height)
        livesLayout.layout(0,0, canvasBreakout.width, canvasBreakout.height)
        livesLayout.draw(canvasBreakout)

        val levelLayout = LinearLayout(App.instance)
        levelLayout.gravity = Gravity.CENTER_HORIZONTAL
        levelLayout.addView(levelText)
        levelLayout.measure(canvasBreakout.width, canvasBreakout.height)
        levelLayout.layout(0,0, canvasBreakout.width, canvasBreakout.height)
        levelLayout.draw(canvasBreakout)

        val scoresLayout = LinearLayout(App.instance)
        scoresLayout.gravity = Gravity.END
        scoresLayout.addView(scoresText)
        scoresLayout.measure(canvasBreakout.width, canvasBreakout.height)
        scoresLayout.layout(0,0, canvasBreakout.width, canvasBreakout.height)
        scoresLayout.draw(canvasBreakout)
    }



    private fun resumeThread() {
        pause = false
        surfaceCreated(holder)
    }

    private fun pauseThread() {
        pause = true
    }



}
//for multiple balls (power up?)
//private var ballA = mutableListOf<BallBreakout>()
//val ballCount = 0

// in setup()
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

// in update()
//        ballA.forEach {
//            it.update(player.posX, player.posY, player.width, player.posXOld)
//        }

// in draw()
//        ballA.forEach {
//            it.draw(canvas)
//        }

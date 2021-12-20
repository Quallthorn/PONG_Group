package com.example.pong_group.Model

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Build
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.example.pong_group.Controller.App
import android.widget.Button
import com.example.pong_group.R
import com.example.pong_group.Services.GameSettings
import com.example.pong_group.Services.GameThread

import android.widget.TextView

import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.example.pong_group.Model.GameViewBreakout.Companion.outOfLives


class GameViewBreakout(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private var thread: GameThread
    private var player: PaddleBreakout
    private var paddlePosY = 0f
    private var ball: BallBreakout
    private var bricks = mutableListOf<Brick>()
    private var rngColor = Paint()

    private lateinit var restartButton: SurfaceViewButton
    var pause = false

    private val colorArray: TypedArray
    private val classic = GameSettings.classicBreakout
    val typeFace = ResourcesCompat.getFont(App.instance, R.font.arcade_classic)
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
    var isButtonClickable = false

    companion object {
        var canvasBreakout = Canvas()
        var totalCountOfBricks = 0

        var outOfLives = false
        var lives = 3
        var breakBuffer = true
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
            brickCountY = 6
        }
        holder.addCallback(this)

        gridPosX = gridSqueezeX
        gridPosY = gridStartY

        player = PaddleBreakout()
        ball = BallBreakout()
        changeColors()

        thread = GameThread(holder, this)

        GameSettings.gameSetUpBreakout(brickCountX, brickCountY)
    }



    private fun setup() {
        gridPosX = gridSqueezeX
        gridPosY = gridStartY
        totalCountOfBricks = brickCountX*brickCountY
        Log.d("SCORES", "totalcount of breaks $totalCountOfBricks")
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

        outOfLives = false
        bricks.clear()

        for (i in 0 until (brickCountX * brickCountY)) {
            //set position
            var newBrick = Brick(brickW, brickH, gridPosX, gridPosY, pointBase, 5)

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

    fun checkSurroundings(pos: Int){
        var onEdge = checkEdge(pos, true)
        if (!onEdge)
            bricks[pos + 1].breakable
        else {
            onEdge = checkEdge(pos, false)
            if (!onEdge)
                bricks[pos + 1].breakable
        }
        bricks[pos + 15].breakable
        bricks[pos - 15].breakable
    }

    private fun checkEdge(pos: Int, right: Boolean): Boolean{
        for (i in 1 until brickCountX step brickCountY){
            if (right && pos + 1 == i)
                return true
            else if (!right && pos - 1 == i)
                return true
        }
        return false
    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.also {
            it.drawColor(Color.BLACK)
            player.draw()
            if (!outOfLives)
                ball.draw()
            bricks.forEach { brick ->
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
                    brickCountY = 9
                } else {
                    lives = 3
                    GameSettings.scoreBreakout = 0
                    level = 1
                    brickCountY = 6
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
            if (totalCountOfBricks == 89 || outOfLives) {
                val layout = LinearLayout(App.instance)
                layout.orientation = LinearLayout.VERTICAL
                layout.gravity = Gravity.CENTER

                var gameOverTextView = TextView(App.instance)

                val textParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1F)

                gameOverTextView.visibility = View.VISIBLE
                if(outOfLives) {
                    gameOverTextView.text = "YOU\nLOSE"
                } else if(level == 1){
                    gameOverTextView.text = "NEXT\nLEVEL"
                }
                else {
                    gameOverTextView.text = "GAME\nOVER"
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

    fun scoresAndLivesCounter() {
        val livesLayout = LinearLayout(App.instance)
        livesLayout.orientation = LinearLayout.HORIZONTAL
        var params = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        livesLayout.layoutParams = params


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

        val scoresLayout = LinearLayout(App.instance)
        scoresLayout.gravity = Gravity.END
        scoresLayout.addView(scoresText)

        livesLayout.addView(livesText)


        livesLayout.measure(canvasBreakout.width, canvasBreakout.height)
        livesLayout.layout(0,0, canvasBreakout.width, canvasBreakout.height)

        scoresLayout.measure(canvasBreakout.width, canvasBreakout.height)
        scoresLayout.layout(0,0, canvasBreakout.width, canvasBreakout.height)

        livesLayout.draw(canvasBreakout)
        scoresLayout.draw(canvasBreakout)
    }



    fun resumeThread() {
        pause = false
        surfaceCreated(holder)
    }

    fun pauseThread() {
        pause = true
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

package com.example.pong_group.Model

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.example.pong_group.Controller.NameInputActivity
import com.example.pong_group.Controller.prefs
import com.example.pong_group.Services.SharedBreakout
import com.example.pong_group.Services.GameSettings.curCanvas
import com.example.pong_group.Services.GameSettings.screenHeight
import com.example.pong_group.Services.GameSettings.screenWidth
import com.example.pong_group.Services.GameSounds
import com.example.pong_group.views.SurfaceViewButton

class GameViewBreakout(context: Context) : SurfaceView(context), SurfaceHolder.Callback {


    private var player: PaddleBreakout
    private var paddlePosY = 0f
    private var ball: BallBreakout

    private lateinit var restartButton: SurfaceViewButton
    var pause = false

    private val colorArray: TypedArray
    private val classic = prefs.isClassicInterface
    private val infinite = prefs.isInfiniteLevels
    private val typeFace = ResourcesCompat.getFont(App.instance, R.font.arcade_classic)
    private var everyOther = false
    private var level = 1
    private var gridPosX: Float = 0f
    private var gridPosY: Float = 0f
    private var gridSqueezeX: Float = 0f
    private var gridStartY: Float = 0f
    private var gridSpacingX: Float = 0f
    private var gridSpacingY: Float = 0f
    private var brickH = 0f
    private var brickW = 0f
    private var isButtonClickable = false

    companion object {
        private const val textSize = 24F
        const val ballEdgeTop = textSize * 5f
        var totalCountOfBricks = 0

        var outOfLives = false
        var lives = 0
        var breakReady = true
        lateinit var thread: GameThread
    }

    init {
        if (classic) {
            lives = 1
            GameSettings.highScoreBreakoutClassic = ScoresRealm.findHighestScore(GameType.CLASSIC)
            SharedBreakout.ballSpeedStart = 10f
            SharedBreakout.brickCountX = 14
            SharedBreakout.brickCountY = 8
            colorArray = App.instance.resources.obtainTypedArray(R.array.breakout_bricks_classic)
        } else {
            if (infinite)
                GameSettings.highScoreBreakoutInfinite = ScoresRealm.findHighestScore(GameType.INFINITE)
            else
                GameSettings.highScoreBreakout = ScoresRealm.findHighestScore(GameType.BREAKOUT)
            colorArray = App.instance.resources.obtainTypedArray(R.array.breakout_bricks)
            lives = 3
            SharedBreakout.ballSpeedStart = 15f
            SharedBreakout.brickCountY = 6
        }

        holder.addCallback(this)
        thread = GameThread(holder, this)

        GameSettings.scoreBreakout = 0
        SharedBreakout.highScoreBroken = false
        SharedBreakout.gameSetUpBreakout()
        player = PaddleBreakout()
        ball = BallBreakout()
        ball.speed = SharedBreakout.ballSpeedStart
        changeColors()

    }

    /**
     * sets up the game for each level
     */
    private fun setup() {
        ball.letGo = false
        SharedBreakout.bricks.clear()
        basedOnScreenSize()
        createBricks()
        ball.centerBall(player.posX, player.posY)
        outOfLives = false
    }

    /**
     * updates all object in play
     */
    fun update() {
        //player.update()
        ball.update(player)
        SharedBreakout.bricks.forEach {
            it.update(ball)
        }
        if (ball.changeColor)
            changeColors()
    }

    /**
     * draws all objects in play
     *
     * @param canvas canvas everything is to be drawn on
     */
    @RequiresApi(Build.VERSION_CODES.M)
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.also {
            it.drawColor(Color.BLACK)
            player.draw()
            SharedBreakout.bricks.forEach { brick ->
                brick.draw()
            }
            if (!outOfLives)
                ball.draw()
            valuesCounter()
        }
    }

    /**
     * determines values for object dimensions according to size of screen
     */
    private fun basedOnScreenSize(){
        gridStartY = screenHeight / 20f + ballEdgeTop
        gridPosX = gridSqueezeX
        gridPosY = gridStartY
        totalCountOfBricks = SharedBreakout.brickCountX * SharedBreakout.brickCountY
        paddlePosY = screenHeight / 7.2f
        player.posY = paddlePosY

        player.width = screenWidth / 13f
        player.height = screenHeight / 100f

        ball.radius = screenHeight / 185f

        if (classic){
            brickH = screenHeight / 98f
            gridSpacingX = screenHeight / 392f
            gridSpacingY = screenHeight / 392f
        } else brickH = screenHeight / 40f

        brickW =
            ((screenWidth - gridSqueezeX * 2) / SharedBreakout.brickCountX) - gridSpacingX
    }

    /**
     * creates an array of bricks
     * the point of the game will be to destroy these bricks
     */
    private fun createBricks(){
        var colorNumber = 1
        var pointBase = SharedBreakout.brickCountY
        if (level > 1 || classic)
            colorNumber = 0
        if (classic) {
            pointBase = 7
        }

        for (i in 0 until (SharedBreakout.brickCountX * SharedBreakout.brickCountY)) {
            //set position
            val newBrick = Brick(brickW, brickH, gridPosX, gridPosY, pointBase, i)

            //make top and bottom rows hit-able
            if (i < SharedBreakout.brickCountX)
                newBrick.exT = true
            else if (i >= SharedBreakout.brickCountX * SharedBreakout.brickCountY - SharedBreakout.brickCountX)
                newBrick.exB = true

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
                if (classic) {
                    everyOther = if (everyOther) {
                        pointBase -= 2
                        false
                    } else
                        true
                } else
                    pointBase--
            }
            SharedBreakout.bricks.add(newBrick)
        }
    }

    /**
     * changes color of paddle and ball if "prefs.isRainbowColor" is true
     */
    private fun changeColors() {
        if (prefs.isRainbowColor) {
            GameSettings.getRandomColorFromArray()
            ball.changeColor = false
        }
    }

    /**
     * creates a thread the game will be run in
     */
    override fun surfaceCreated(p0: SurfaceHolder) {
        if (thread.state == Thread.State.TERMINATED) {
            thread = GameThread(holder, this)
        }
        thread.running = true
        thread.start()
        GameSettings.scoreBreakout = 0
    }

    /**
     * measures screen dimensions and calls setup()
     */
    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        GameSettings.setScreenDimen(p2.toFloat(), p3.toFloat())
        setup()
    }

    /**
     * closes thread when game is exited
     */
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

    /**
     * keeps track of finger inputs from user
     *
     * if player lost a life ball will be "held" by paddle until player releases finger from screen
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            player.posX = event.x
            if (event.action == MotionEvent.ACTION_UP){
                GameSounds.playWall()
                ball.letGo = true
            }
        }

        val x = event?.x
        val y = event?.y

        if (isButtonClickable) {
            if (restartButton.btnRect!!.contains(x!!, y!!)) {
                isButtonClickable = false
                if (level == 1 && !outOfLives || infinite && !outOfLives) {
                    level += 1
                    if (!classic) {
                        SharedBreakout.brickCountY = 9
                    } else {
                        SharedBreakout.brickCountY = 8
                    }
                } else {
                    GameSettings.savedScore = GameSettings.scoreBreakout
                    context.startActivity(Intent(context, NameInputActivity::class.java))
                }
                setup()
                resumeThread()
            }
        }
        return true
    }

    /**
     * checks if user completed the game, lost or advanced to the next level
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun checkEndOfTheGame() {
        //game over layout
        if (totalCountOfBricks <= 0 || outOfLives) {
            val layout = LinearLayout(App.instance)
            layout.orientation = LinearLayout.VERTICAL
            layout.gravity = Gravity.CENTER

            val gameOverTextView = TextView(App.instance)

            val textParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1F)

            gameOverTextView.visibility = View.VISIBLE
            when {
                outOfLives -> {
                    gameOverTextView.text =
                        App.instance.getString(R.string.game_over_text, "YOU\nLOSE")
                }
                level == 1 || infinite -> {
                    gameOverTextView.text =
                        App.instance.getString(R.string.game_over_text, "NEXT\nLEVEL")
                }
                else -> {
                    gameOverTextView.text =
                        App.instance.getString(R.string.game_over_text, "GAME\nOVER")
                }
            }
            gameOverTextView.textSize = 100f
            gameOverTextView.setLineSpacing(0f, 0.7f)
            gameOverTextView.gravity = Gravity.CENTER
            gameOverTextView.typeface = typeFace
            gameOverTextView.layoutParams = textParams
            gameOverTextView.setTextColor(
                App.instance.resources.getColor(
                    R.color.white,
                    context.theme
                )
            )

            gameOverTextView.measure(0, 0)
            val gameTextViewHeight = gameOverTextView.measuredHeight

            layout.addView(gameOverTextView)

            layout.measure(curCanvas.width, curCanvas.height)
            layout.layout(0, 0, curCanvas.width, curCanvas.height)
            layout.draw(curCanvas)

            //restart button layout

            if (level == 1 && !outOfLives || prefs.isInfiniteLevels && !outOfLives) {
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

    /**
     * creates a button on canvas
     */
    private fun setupButton(icon: Bitmap, textViewHeight: Int) {
        restartButton = SurfaceViewButton(icon)
        val restartButtonX = (curCanvas.width / 2).toFloat() - restartButton.width / 2
        val restartButtonY = (curCanvas.height / 2).toFloat() + textViewHeight / 2 + 40f
        restartButton.setPosition(restartButtonX, restartButtonY)
        restartButton.draw()
    }

    /**
     * creates text on canvas displaying:
     * lives, level, score and high score
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun valuesCounter() {
        //Left Side
        val livesLevelLayout = LinearLayout(App.instance)
        livesLevelLayout.orientation = LinearLayout.VERTICAL

        val textParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, 1F)
        val livesText = TextView(App.instance)
        livesText.text = context.getString(R.string.lives_text, lives)
        livesText.textSize = textSize
        livesText.typeface = typeFace
        livesText.layoutParams = textParams
        livesText.setTextColor(App.instance.resources.getColor(R.color.white, context.theme))

        val levelText = TextView(App.instance)
        levelText.text = context.getString(R.string.level_text, level)
        levelText.textSize = textSize
        levelText.typeface = typeFace
        levelText.layoutParams = textParams
        levelText.setTextColor(App.instance.resources.getColor(R.color.white, context.theme))

        livesLevelLayout.addView(livesText)
        livesLevelLayout.addView(levelText)
        livesLevelLayout.gravity = Gravity.START
        livesLevelLayout.measure(curCanvas.width, curCanvas.height)
        livesLevelLayout.layout(0, 0, curCanvas.width, curCanvas.height)
        livesLevelLayout.draw(curCanvas)


        //Right Side
        val scoresLayout = LinearLayout(App.instance)
        scoresLayout.orientation = LinearLayout.VERTICAL

        val highScoreText = TextView(App.instance)
        if (SharedBreakout.highScoreBroken){
            highScoreText.setTextColor(
                App.instance.resources.getColor(
                    R.color.yellow,
                    context.theme
                )
            )
            highScoreText.text =
                context.getString(R.string.high_scores_text, GameSettings.scoreBreakout)
        }
        else{
            highScoreText.setTextColor(
                App.instance.resources.getColor(
                    R.color.white,
                    context.theme
                )
            )
            when {
                classic -> highScoreText.text =
                    context.getString(R.string.high_scores_text, GameSettings.highScoreBreakoutClassic)
                infinite -> highScoreText.text =
                    context.getString(R.string.high_scores_text, GameSettings.highScoreBreakoutInfinite)
                else -> highScoreText.text =
                    context.getString(R.string.high_scores_text, GameSettings.highScoreBreakout)
            }
        }
        highScoreText.textSize = textSize
        highScoreText.gravity = Gravity.END
        highScoreText.typeface = typeFace
        livesText.layoutParams = textParams

        val scoreText = TextView(App.instance)
        scoreText.text = context.getString(R.string.score_text, GameSettings.scoreBreakout)
        scoreText.textSize = textSize
        scoreText.gravity = Gravity.END
        scoreText.typeface = typeFace
        livesText.layoutParams = textParams
        scoreText.setTextColor(App.instance.resources.getColor(R.color.white, context.theme))

        scoresLayout.addView(highScoreText)
        scoresLayout.addView(scoreText)
        scoresLayout.gravity = Gravity.END
        scoresLayout.measure(curCanvas.width, curCanvas.height)
        scoresLayout.layout(0, 0, curCanvas.width, curCanvas.height)
        scoresLayout.draw(curCanvas)
    }


    /**
     * currently not used but sets pause bool to false when returning to thread
     */
    private fun resumeThread() {
        pause = false
        surfaceCreated(holder)
    }

    /**
     * currently not used but sets pause bool to true when thread is paused
     */
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

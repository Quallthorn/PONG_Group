package com.example.pong_group.services


import android.util.Log
import android.view.SurfaceHolder
import com.example.pong_group.model.GameViewBreakout
import com.example.pong_group.services.GameSettings.curCanvas

//custom game thread for changing gameFPS and run game movies inside of thread
class GameThread(
    private val surfaceHolder: SurfaceHolder,
    private val gameView: GameViewBreakout
) : Thread() {

    //number of FPS for thread
    private val targetFPS = 60
    var running: Boolean = false

    init {
        name = "GameThread"
    }

    //with this func we can run every animation in breakout game
    override fun run() {
        var startTime: Long
        val targetTime = (1000 / targetFPS).toLong()
        var cLocked = false


        //check if game still running or pause
        while (running) {
            startTime = System.nanoTime()
            try {
                curCanvas = surfaceHolder.lockCanvas()?.also {
                    cLocked = true
                    synchronized(surfaceHolder) {
                        if (gameView.pause) {
                            running = false
                        }
                        //update canvas and redraw
                        gameView.update()
                        gameView.draw(it)
                    }

                    //every frame we check conditions for finishing game
                    gameView.checkEndOfTheGame()

                    surfaceHolder.unlockCanvasAndPost(it)

                    cLocked = false

                }!!
            } catch (ignored: Exception) {

                Log.d("ERROR", "$ignored")

                //special feature to prevent blocking canvas
                if (!cLocked) {
                    curCanvas = this.surfaceHolder.lockCanvas()
                    cLocked = true
                }
                if (cLocked) {
                    surfaceHolder.unlockCanvasAndPost(curCanvas)

                    cLocked = false
                }
            }

            try {
                sleep(targetTime - (System.nanoTime() - startTime) / 1000000)
            } catch (ignored: Exception) {
            }
        }
    }
}


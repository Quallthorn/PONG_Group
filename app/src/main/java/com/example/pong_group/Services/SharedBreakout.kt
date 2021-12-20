package com.example.pong_group.Services

import android.util.Log
import com.example.pong_group.Model.Brick
import kotlin.properties.Delegates

object SharedBreakout {
    var bricks = mutableListOf<Brick>()
    var brickCountX: Int = 15
    var brickCountY: Int = 0

    //Classic
    const val ballSpeedStart = 10f
    var ballSpeed = ballSpeedStart
    private const val speedIncrease = 4f
    var hits = 0
    private var orangeHit = false
    private var redHit = false
    var upperWallHit = false
    var maxSpeedAchieved = false

    //non-classic
    var brickCounts = mutableListOf<Int>()
    var lowestBrick by Delegates.notNull<Int>()
    var totalRows by Delegates.notNull<Int>()

    fun gameSetUpBreakout() {
        brickCounts.clear()
        for (i in 0 until brickCountY)
            brickCounts.add(brickCountX)
        lowestBrick = brickCountY - 1
        totalRows = brickCountY
    }

    fun checkSurroundings(nr: Int) {
        var onEdge: Boolean

        //right of
        if (nr + 1 < brickCountX * brickCountY){
            onEdge = checkEdge(nr + 1, true)
            if (!onEdge) {
                bricks[nr + 1].canBreak()
            }
        }

        //left of
        if (nr - 1 >= 0){
            onEdge = checkEdge(nr - 1, false)
            if (!onEdge) {
                bricks[nr - 1].canBreak()
            }
        }

        //above
        if (nr - brickCountX >= 0) {
            bricks[nr - brickCountX].canBreak()
        }

        //below
        if (nr + brickCountX < brickCountX * brickCountY) {
            bricks[nr + brickCountX].canBreak()
        }

        if (nr < brickCountX)
            upperRowBreakable()
    }

    private fun upperRowBreakable() {
        for (i in 0 until brickCountX)
            bricks[i].canBreak()
    }

    private fun checkEdge(nr: Int, right: Boolean): Boolean {
        if (right){
            for (i in 0..brickCountX*brickCountY step brickCountX) {
                if (nr == i)
                    return true
            }
        }
        else{
            for (i in brickCountX - 1..brickCountX*brickCountY step brickCountX) {
                if (nr == i)
                    return true
            }
        }
        return false
    }

    fun addScore(pointBase: Int) {
        GameSettings.scoreBreakout += pointBase
        if (pointBase + lowestBrick != totalRows)
            GameSettings.scoreBreakout += lowestBrick * brickCounts[lowestBrick]
        brickCounts[pointBase]--
        while (brickCounts[lowestBrick] == 0 && lowestBrick > 0)
            lowestBrick--

        Log.d("scoreNormal", "$GameSettings.scoreBreakout")
    }

    fun addScoreClassic(pointBase: Int) {
        GameSettings.scoreBreakout += pointBase
        Log.d("classic", "Points: $GameSettings.scoreBreakout")
    }

    fun updateSpeedClassic(brickColor: Int) {
        if (hits <= 12)
            hits++
        if (hits == 4 || hits == 12)
            ballSpeed += speedIncrease
        if (brickColor == 5 && !orangeHit) {
            ballSpeed += speedIncrease
            orangeHit = true
        }
        if (brickColor == 7 && !redHit) {
            ballSpeed += speedIncrease
            redHit = true
        }
        if (ballSpeed >= ballSpeedStart + speedIncrease * 4) {
            ballSpeed = ballSpeedStart + speedIncrease * 4
            maxSpeedAchieved = true
        }
        Log.d("classic", "Hits: $hits")
        Log.d("classic", "Speed: $ballSpeed")
    }
}
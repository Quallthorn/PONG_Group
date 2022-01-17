package com.example.pong_group.Model

import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmQuery
import io.realm.Sort
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId
import java.util.Collections.list

enum class GameType(val gameType: String){
    CLASSIC("classic"),
    INFINITE("infinite"),
    BREAKOUT("breakout")
}

open class ScoresRealm(
    @PrimaryKey
    var id: String = ObjectId().toHexString(),
    @Required
    var name: String = "",
    var scores: Int = 0,
    var gameType: String = ""
) : RealmObject() {

    companion object {
        fun addScores(scores: Int, name: String, type: GameType) {
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction {
                val newScores = ScoresRealm(name = name, scores = scores, gameType = type.gameType)
                it.insert(newScores)
            }
        }

        fun retrieveScores(type: GameType): MutableList<Scores> {
            var gameType  = type.gameType
            val realm = Realm.getDefaultInstance()
            val scoresList = mutableListOf<Scores>()

            realm.executeTransaction { realmTransaction ->
                scoresList.addAll(realmTransaction
                    .where(ScoresRealm::class.java)
                    .equalTo("gameType", gameType)
                    .sort("scores", Sort.DESCENDING)
                    .findAll()
                    .map {
                        mapScores(it)
                    }

                )
            }
            return scoresList
        }

        fun findHighestScore(type: GameType): Int {
            val realm = Realm.getDefaultInstance()
            var highScore = 0

            realm.executeTransaction { realmTransaction ->
                val high = realmTransaction.where(ScoresRealm::class.java)
                    .equalTo("gameType", type.gameType)
                    .max("scores")
                highScore = high?.toInt() ?: 0
            }
            return highScore
        }

        fun findRank(score: Int, type: GameType): Int {
            val realm = Realm.getDefaultInstance()
            var rank = 1

            realm.executeTransaction { realmTransaction ->
                val list = realmTransaction.where(ScoresRealm::class.java)
                    .equalTo("gameType", type.gameType)
                    .sort("scores", Sort.DESCENDING)

                for (i in 0 until list.count().toInt()) {
                    if (score <= list.findAll().get(i)?.scores ?: 0){
                        rank++
                    }
                    else
                        break
                }
            }
            return rank
        }

        private fun mapScores(scores: ScoresRealm): Scores {
            return Scores(
                scores.id,
                scores.name,
                scores.scores,
                scores.gameType
            )
        }
    }
}

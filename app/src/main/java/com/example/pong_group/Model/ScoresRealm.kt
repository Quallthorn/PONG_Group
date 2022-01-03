package com.example.pong_group.Model

import android.util.Log
import io.realm.Realm
import io.realm.RealmObject
import io.realm.Sort
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId


open class ScoresRealm(
    @PrimaryKey
    var id: String = ObjectId().toHexString(),
    @Required
    var name: String = "",
    var scores: Int = 0,
    var gameType: String = ""
) : RealmObject() {

    companion object {
        fun addScores(scores: Int, name: String, type: String) {
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction {
                val newScores = ScoresRealm(name = name, scores = scores, gameType = type)
                it.insert(newScores)
            }
        }

        fun retrieveScores(type: String): MutableList<Scores> {
            val realm = Realm.getDefaultInstance()
            val scoresList = mutableListOf<Scores>()

            realm.executeTransaction { realmTransaction ->
                scoresList.addAll(realmTransaction
                    .where(ScoresRealm::class.java)
                    .equalTo("gameType", type)
                    .sort("scores", Sort.DESCENDING)
                    .findAll()
                    .map {
                        mapScores(it)
                    }

                )
            }
            return scoresList
        }

        fun findHighestScore(type: String): Int {
            val realm = Realm.getDefaultInstance()
            var highScore = 0

            realm.executeTransaction { realmTransaction ->
                val high = realmTransaction.where(ScoresRealm::class.java)
                    .equalTo("gameType", type)
                    .max("scores")
                highScore = high?.toInt()?:0
            }
        return  highScore
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
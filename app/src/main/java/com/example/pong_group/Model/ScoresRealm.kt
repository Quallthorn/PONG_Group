package com.example.pong_group.Model

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId



open class ScoresRealm(
    @PrimaryKey
    var id: String = ObjectId().toHexString(),
    @Required
    var name: String = "",
    var scores: Int = 0,
    var gameType: String = "",
    var rank: Int = 0
): RealmObject() {

    companion object{
        fun addScores(scores: Int, name: String, type: String, rank: Int) {
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction {
                val newScores = ScoresRealm( name = name, scores = scores, gameType =  type, rank =  rank)
                it.insert(newScores)
            }
        }

        fun retrieveScores(type: String): MutableList<Scores> {
            val realm  = Realm.getDefaultInstance()
            val scoresList = mutableListOf<Scores>()

            realm.executeTransaction { realmTransaction ->
                scoresList.addAll( realmTransaction
                    .where(ScoresRealm::class.java)
                    .equalTo("gameType", type)
                    .findAll()
                    .map {
                        mapScores(it)
                    }

                )
            }
            return  scoresList
        }

        private fun mapScores(scores: ScoresRealm): Scores {
            return Scores(
                scores.id,
                scores.name,
                scores.scores,
                scores.gameType,
                scores.rank
            )
        }
    }

}
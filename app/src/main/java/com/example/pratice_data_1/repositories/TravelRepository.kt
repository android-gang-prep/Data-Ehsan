package com.example.pratice_data_1.repositories

import com.example.pratice_data_1.db.entities.Travel
import com.example.pratice_data_1.model.UiTravel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TravelRepository(private val realm: Realm) {
    fun getTravel(travelId: String): Flow<UiTravel?> {
        return realm.query<Travel>("id == $0", travelId).first().asFlow().map { it.obj?.toUiModel() }
    }

    fun getTravels(): Flow<List<UiTravel>> {
        return realm.query<Travel>().find().asFlow().map { it.list.map { it.toUiModel() } }
    }

    suspend fun addTravel(travel: UiTravel): Travel {
        return realm.write {
            copyToRealm(travel.toEntity())
        }
    }

    suspend fun updateTravel(travel: UiTravel) {

        println("repository id: ${travel.id}")
        val liveTravel = realm.query<Travel>("id == $0", travel.toEntity().id)
            .find()
            .first()
        realm.write {
            findLatest(liveTravel)?.let {
                it.users = travel.toEntity().users
                it.costs = travel.toEntity().costs
                it.debts = travel.toEntity().debts
            }
        }
    }
}
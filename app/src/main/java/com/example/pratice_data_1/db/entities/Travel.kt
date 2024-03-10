package com.example.pratice_data_1.db.entities

import com.example.pratice_data_1.model.UiTravel
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID


class Travel:RealmObject{
    @PrimaryKey
    var id:String = UUID.randomUUID().toString()
    var name:String = ""
    var users:RealmList<User> = realmListOf()
    var costs:RealmList<Cost> = realmListOf()
    var debts:RealmList<Debt> = realmListOf()

    fun toUiModel():UiTravel{
        return UiTravel(
            id = id,
            name = name,
            users = users.map { it.toUiModel() },
            costs = costs.map { it.toUiModel() },
            debts = debts.map { it.toUiModel() }
        )
    }
}

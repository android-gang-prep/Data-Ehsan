package com.example.pratice_data_1.db.entities

import com.example.pratice_data_1.model.UiCost
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class Cost:RealmObject{
    @PrimaryKey
    var id:String = UUID.randomUUID().toString()
    var title:String = ""
    var userCosts:RealmList<UserCost> = realmListOf()
    var payer:User? = null
    var payDate:String? = null


    fun toUiModel():UiCost{
        return UiCost(
            id = id,
            title = title,
            userCosts = userCosts.map { it.toUiModel() },
            payer = payer?.toUiModel(),
            payDate = payDate
        )
    }
}

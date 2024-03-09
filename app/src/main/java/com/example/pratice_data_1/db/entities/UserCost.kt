package com.example.pratice_data_1.db.entities

import com.example.pratice_data_1.model.UiUserCost
import com.example.pratice_data_1.utils.PriceType
import io.realm.kotlin.types.RealmObject
import java.util.UUID

class UserCost:RealmObject{
    var id:String = UUID.randomUUID().toString()
    var user:User? = User()
    var amount:String = "0.0"
    var priceType:String = PriceType.PRICE_TYPE_IRT

    fun toUiModel():UiUserCost{
        return UiUserCost(
            id = id,
            user = user?.toUiModel(),
            amount = amount,
            priceType = priceType
        )
    }
}

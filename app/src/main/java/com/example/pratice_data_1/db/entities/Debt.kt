package com.example.pratice_data_1.db.entities

import com.example.pratice_data_1.model.UiDebt
import com.example.pratice_data_1.utils.PriceType
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class Debt : RealmObject {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()

    var debt: String = "0.0"
    var deptType: String = PriceType.PRICE_TYPE_IRT
    var debtFrom: User? = null
    var debtTo: User? = null

    fun toUiModel(): UiDebt {
        return UiDebt(
            id = id,
            debt = debt,
            debtType = deptType,
            debtFrom = debtFrom?.toUiModel(),
            debtTo = debtTo?.toUiModel()
        )
    }
}
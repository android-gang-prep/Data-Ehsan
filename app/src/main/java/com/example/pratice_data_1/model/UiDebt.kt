package com.example.pratice_data_1.model

import com.example.pratice_data_1.db.entities.Debt
import com.example.pratice_data_1.utils.PriceType
import java.util.UUID

data class UiDebt(
    var id:String = UUID.randomUUID().toString(),
    var debt:String = "0.0",
    var debtType:String = PriceType.PRICE_TYPE_IRT,
    var debtFrom:UiUser? = null,
    var debtTo:UiUser? = null
){
    fun toEntity():Debt{
        return Debt().apply {
            id = this@UiDebt.id
            debt = this@UiDebt.debt
            deptType = this@UiDebt.debtType
            debtFrom = this@UiDebt.debtFrom?.toEntity()
            debtTo = this@UiDebt.debtTo?.toEntity()
        }
    }
}

package com.example.pratice_data_1.repositories

import androidx.lifecycle.ViewModel
import com.example.pratice_data_1.db.entities.Log
import com.example.pratice_data_1.model.UiLog
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LogRepository(private val realm: Realm) {

    fun getLogs():Flow<List<UiLog>>{
        return realm.query<Log>().find().asFlow().map { it.list.map { it.toUiModel() } }
    }
    suspend fun deleteLogs(){
        realm.write {
            delete(Log::class)
        }
    }

    suspend fun addLog(log:String){
        realm.write {
            copyToRealm(Log().apply {
                this.log = log
            })
        }
    }

}
suspend fun KoinComponent.addLog(str:String){
    val repository:LogRepository by inject()
    repository.addLog(str)
}
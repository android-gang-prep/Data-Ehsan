package com.example.pratice_data_1

import android.app.Application
import com.example.pratice_data_1.db.entities.Cost
import com.example.pratice_data_1.db.entities.Debt
import com.example.pratice_data_1.db.entities.Log
import com.example.pratice_data_1.db.entities.Travel
import com.example.pratice_data_1.db.entities.User
import com.example.pratice_data_1.db.entities.UserCost
import com.example.pratice_data_1.repositories.CostRepository
import com.example.pratice_data_1.repositories.LogRepository
import com.example.pratice_data_1.repositories.TravelRepository
import com.example.pratice_data_1.repositories.UserCostRepository
import com.example.pratice_data_1.repositories.UserRepository
import io.realm.kotlin.Configuration
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(module {
                single<Configuration>{
                    RealmConfiguration.Builder(
                        schema = setOf(
                            User::class,
                            Travel::class,
                            Cost::class,
                            UserCost::class,
                            Log::class,
                            Debt::class
                        )
                    ).name(name = "app_db")
                        .schemaVersion(schemaVersion = 1)
                        .deleteRealmIfMigrationNeeded()
                        .build()
                }
                single {
                    Realm.compactRealm(get())
                    Realm.open(get())
                }

                single {
                    UserRepository(get())
                }
                single {
                    CostRepository(get())
                }
                single {
                    UserCostRepository(get())
                }
                single {
                    LogRepository(get())
                }
                single {
                    TravelRepository(get())
                }
            })
        }
    }
}
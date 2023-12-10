package com.swaminarayan.shikshapatriApp.di

import android.app.Application
import androidx.room.Room
import com.swaminarayan.shikshapatriApp.data.DB
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.data.repository.DailyReportRepo
import com.swaminarayan.shikshapatriApp.data.repository.GoalsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun getDB(app: Application) : DB {
        return Room.databaseBuilder(
            app,
            DB::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun getAgnaRepo(db: DB) : AgnaRepo {
        return AgnaRepo(db.agnaDAO)
    }

    @Provides
    @Singleton
    fun getGoalsRepo(db: DB) : GoalsRepo {
        return GoalsRepo(db.goalsDAO)
    }

    @Provides
    @Singleton
    fun getDailyReportRepo(db: DB) : DailyReportRepo {
        return DailyReportRepo(db.dailyReportDAO)
    }



}
package com.swaminarayan.shikshapatriApp.di

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.swaminarayan.shikshapatriApp.data.DB
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.data.repository.DSRepo
import com.swaminarayan.shikshapatriApp.data.repository.DailyFormRepo
import com.swaminarayan.shikshapatriApp.data.repository.NoteRepo
import com.swaminarayan.shikshapatriApp.domain.usecases.DeleteAgna
import com.swaminarayan.shikshapatriApp.domain.usecases.UseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun getDB(app: Application): DB {
        return Room.databaseBuilder(
            app,
            DB::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun getAgnaRepo(db: DB): AgnaRepo {
        return AgnaRepo(db.agnaDAO)
    }

    @Provides
    @Singleton
    fun getDailyAgnaFormReportRepo(db: DB): DailyFormRepo {
        return DailyFormRepo(db.dailyFormDAO)
    }

    @Provides
    @Singleton
    @RequiresApi(Build.VERSION_CODES.O)
    fun dsRepo(@ApplicationContext context: Context): DSRepo {
        return DSRepo(context)
    }

    @Provides
    @Singleton
    fun notesRepo(db: DB): NoteRepo {
        return NoteRepo(db.noteDAO)
    }

    @Provides
    @Singleton
    fun useCases(
        agnaRepo: AgnaRepo,
        dailyFormRepo: DailyFormRepo
    ): UseCases {
        return UseCases(
            deleteAgna = DeleteAgna(
                agnaRepo = agnaRepo,
                dailyFormRepo = dailyFormRepo
            )
        )
    }

}
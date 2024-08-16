package com.example.shikshapatriapp.di

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.shikshapatriapp.data.Database
import com.example.shikshapatriapp.data.repository.AgnaRepo
import com.example.shikshapatriapp.data.repository.DSRepo
import com.example.shikshapatriapp.data.repository.DailyAgnaRepo
import com.example.shikshapatriapp.data.repository.DailyFormRepo
import com.example.shikshapatriapp.data.repository.NoteRepo
import com.example.shikshapatriapp.domain.usecase.GetDailyFormWithDailyAgnaUseCase
import com.example.shikshapatriapp.domain.usecase.UseCases
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
    fun getDB(app: Application): Database {
        return Room.databaseBuilder(
            app,
            Database::class.java,
            "shikshapatri_database"
        )
            .build()
    }

    @Provides
    @Singleton
    fun getAgnaRepo(db: Database): AgnaRepo {
        return AgnaRepo(db.agnaDAO)
    }

    @Provides
    @Singleton
    fun getDailyAgnaFormReportRepo(db: Database): DailyFormRepo {
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
    fun notesRepo(db: Database): NoteRepo {
        return NoteRepo(db.noteDAO)
    }

    @Provides
    @Singleton
    fun dailyAgnaRepo(db: Database): DailyAgnaRepo {
        return DailyAgnaRepo(db.dailyAgnaDAO)
    }

    @Provides
    @Singleton
    fun getUseCases(dailyFormRepo: DailyFormRepo, agnaRepo: AgnaRepo): UseCases {
        return UseCases(
            getDailyFormWithDailyAgnaUseCase = GetDailyFormWithDailyAgnaUseCase(
                dailyFormRepo = dailyFormRepo,
                agnaRepo = agnaRepo
            )
        )
    }

}
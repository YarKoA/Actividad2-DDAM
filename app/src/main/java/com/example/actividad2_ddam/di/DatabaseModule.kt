package com.example.actividad2_ddam.di

import android.content.Context
import androidx.room.Room
import com.example.actividad2_ddam.data.AppDatabase
import com.example.actividad2_ddam.data.MIGRATION_2_3
import com.example.actividad2_ddam.data.NotaDao
import com.example.actividad2_ddam.data.TareaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "actividad_database"
        )
            .addMigrations(MIGRATION_2_3)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideNotaDao(database: AppDatabase): NotaDao {
        return database.notaDao()
    }

    @Provides
    fun provideTareaDao(database: AppDatabase): TareaDao {
        return database.tareaDao()
    }
}
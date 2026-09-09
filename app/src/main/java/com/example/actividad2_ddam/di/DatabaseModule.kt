package com.example.actividad2_ddam.di

import android.content.Context
import androidx.room.Room
import com.example.actividad2_ddam.data.AppDatabase
import com.example.actividad2_ddam.data.NotaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Módulo Hilt para proveer la base de datos Room SQLite y sus DAOs
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "notas_sqlite_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideNotaDao(appDatabase: AppDatabase): NotaDao {
        return appDatabase.notaDao()
    }
}

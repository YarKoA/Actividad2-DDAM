package com.example.actividad2_ddam.di // O tu paquete de inyección de dependencias

import android.content.Context
import androidx.room.Room
import com.example.actividad2_ddam.data.AppDatabase
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
            "actividad_database" // Nombre de tu archivo de base de datos
        ).fallbackToDestructiveMigration().build()
    }

    // Proveedor para las notas (si ya lo tenías, déjalo)
    @Provides
    fun provideNotaDao(database: AppDatabase): NotaDao {
        return database.notaDao()
    }

    // ¡EL NUEVO PROVEEDOR QUE FALTA PARA LAS TAREAS!
    @Provides
    fun provideTareaDao(database: AppDatabase): TareaDao {
        return database.tareaDao()
    }
}


/*
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


 */
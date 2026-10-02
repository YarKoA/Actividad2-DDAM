package com.example.actividad2_ddam.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class SettingsManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.dataStore

    val modoOscuroFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[MODO_OSCURO_KEY] ?: false
    }

    val letraGrandeFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[LETRA_GRANDE_KEY] ?: false
    }

    val grosorGruesoFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[GROSOR_GRUESO_KEY] ?: false
    }

    suspend fun setModoOscuro(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[MODO_OSCURO_KEY] = enabled
        }
    }

    suspend fun setLetraGrande(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[LETRA_GRANDE_KEY] = enabled
        }
    }

    suspend fun setGrosorGrueso(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[GROSOR_GRUESO_KEY] = enabled
        }
    }

    companion object {
        val MODO_OSCURO_KEY = booleanPreferencesKey("modo_oscuro")
        val LETRA_GRANDE_KEY = booleanPreferencesKey("letra_grande")
        val GROSOR_GRUESO_KEY = booleanPreferencesKey("grosor_grueso")
    }
}

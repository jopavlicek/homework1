@file:Suppress("SpellCheckingInspection")
package cz.mendelu.pef.petstore.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first

class DataStoreRepositoryImpl(private val context: Context) : IDataStoreRepository {

    override suspend fun setLoginSuccessful(value: Boolean) {
        val preferencesKey = booleanPreferencesKey(DataStoreConstants.LOGIN_SUCCESSFUL)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun getLoginSuccessful(): Boolean {
        return try {
            val preferencesKey = booleanPreferencesKey(DataStoreConstants.LOGIN_SUCCESSFUL)
            val preferences = context.dataStore.data.first()
            if (!preferences.contains(preferencesKey))
                false // true
            else
                preferences[preferencesKey]!!
        } catch (e: Exception) {
            e.printStackTrace()
            false // true
        }
    }
}
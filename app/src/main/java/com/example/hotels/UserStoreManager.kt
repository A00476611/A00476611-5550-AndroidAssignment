package com.example.hotels

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.hotels.models.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object keys {
    val NAME = stringPreferencesKey("userName")
    val EMAIL = stringPreferencesKey("userEmail")
    val ID = intPreferencesKey("userId")
    val PASSWORD = stringPreferencesKey("userPassword")
}
suspend fun saveUser(context:Context, user: User){
    UserStoreManager.getInstance(context).edit { pref ->
        pref[keys.NAME] = user.name
        pref[keys.EMAIL] = user.email
        pref[keys.ID] = user.id
        pref[keys.PASSWORD] = user.password
    }
}
suspend fun getUser(context: Context) : User? {
    val values = UserStoreManager.getInstance(context).data.first()
    if(values[keys.ID] == null || values[keys.NAME] == null || values[keys.EMAIL] == null) return null
    return User(values[keys.ID]!!.toInt(),values[keys.NAME]!!.toString(),values[keys.EMAIL]!!.toString(), values[keys.PASSWORD]!!.toString())
}

suspend fun clearUser(context: Context) {
    UserStoreManager.getInstance(context).edit { it.clear() }
}
object UserStoreManager {
    private val Context.datastore :DataStore<Preferences> by preferencesDataStore(name="user")
    private val instance : DataStore<Preferences>? = null


    fun getInstance(context:Context): DataStore<Preferences>{
        return instance ?: synchronized(this) {
            instance ?: context.applicationContext.datastore
        }
    }

}

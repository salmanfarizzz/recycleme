package com.example.recycleme.data

import android.content.Context
import com.example.recycleme.data.remote.retrofit.ApiConfig
import com.example.recycleme.data.remote.user.UserPref
import com.example.recycleme.data.remote.user.UserRepository
import com.example.recycleme.data.remote.user.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPref.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, pref)
    }
}
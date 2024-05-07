package com.ankitgupta.kchatapp.sharedpref

import android.content.Context
import android.content.SharedPreferences
import com.ankitgupta.kchatapp.model.ProfileData
import com.ankitgupta.kchatapp.application.HiltApplication.Companion.SHARED_PREF_NAME
import com.google.gson.Gson

class ProfileDataManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    // Save ProfileData to SharedPreferences
    fun saveProfileData(profileData: ProfileData, key: String) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(profileData)
        editor.putString(key, json)
        editor.apply()
    }

    // Retrieve ProfileData from SharedPreferences
    fun getProfileData(key: String): ProfileData? {
        val gson = Gson()
        val json = sharedPreferences.getString(key, null)
        return gson.fromJson(json, ProfileData::class.java)
    }

}


package com.robosoft.playverse.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SessionClass{

    fun setNewUserData(context: Context,data : String){
        val sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putString("newUser", data).apply()
    }

    fun getNewUserData(context: Context) : String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val json = prefs.getString("newUser", null)
        val type = object : TypeToken<String>() {
        }.type
        return if(json == null)
            null
        else
            gson.fromJson(json, type)
    }

    fun getClear(context: Context, key: String){
        val preferences: SharedPreferences =
            context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }


}
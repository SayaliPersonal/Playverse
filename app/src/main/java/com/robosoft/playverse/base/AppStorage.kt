package com.robosoft.playverse.base

import android.app.Application
import android.content.Context
import com.robosoft.playverse.utilities.debugLog
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppStorage @Inject constructor(
    val app: Application
) {
    private val pref =
        app.getSharedPreferences("check", Context.MODE_PRIVATE)

    var userId: String
        get() = getString("hh") ?: ""
        set(value) {
            setString("hh", value)
        }

    var accessToken: String
        get() = getString("accessToken") ?: ""
        set(value) {
            setString("accessToken", value)
        }

    var phoneNo: String
        get() = getString("phoneNo") ?: ""
        set(value) {
            setString("phoneNo", value)
        }

    var profilePic: String
        get() = getString("profilePic") ?: ""
        set(value) {
            setString("profilePic", value)
        }

    private fun getString(key: String, defValue: String? = null): String? {
        return pref.getString(key, defValue)
    }

    private fun setString(key: String, value: String?) {
        pref.edit().putString(key, value).apply()
    }


}
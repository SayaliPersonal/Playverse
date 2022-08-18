package com.robosoft.playverse.utilities

import androidx.lifecycle.MutableLiveData
import okhttp3.MultipartBody
import java.util.*

object TemporaryStorage {
    var otp: String = ""
        set(value) {
            field = value
            debugLog("Otp : $value")
        }

    var loginType: String = ""
        set(value) {
            field = value
            debugLog("loginType : $value")
        }

    var image: MultipartBody.Part? = null
        set(value) {
            field = value
            debugLog("Image : $value")
        }

    var date: String = ""
        set(value) {
            field = value
            debugLog("date : $value")
        }


    val check: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val networkError: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

}
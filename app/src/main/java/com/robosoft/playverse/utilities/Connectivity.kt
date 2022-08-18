package com.robosoft.playverse.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.util.Log
import java.util.*

object Connectivity {
    fun isNetworkAvailable(context: Context?): Boolean {
        var isNetworkAvailable = false
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {

            val netInfo = cm.activeNetworkInfo
            if (netInfo != null) {
                isNetworkAvailable = netInfo.isConnectedOrConnecting
            }
        } catch (ex: Exception) {
            Log.e("Network Avail Error", Objects.requireNonNull(ex.message).toString())
        }

        //check for wifi also
        if (!isNetworkAvailable) {
            val connec = context.applicationContext
                .getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifi = Objects.requireNonNull(cm.getNetworkInfo(1))?.state
            isNetworkAvailable =
                connec.isWifiEnabled && wifi.toString().equals("CONNECTED", ignoreCase = true)

        }
        return isNetworkAvailable
    }
}
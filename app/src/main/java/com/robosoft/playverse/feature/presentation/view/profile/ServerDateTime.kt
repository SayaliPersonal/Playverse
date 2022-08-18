package com.robosoft.playverse.feature.presentation.view.profile

import android.app.Activity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class ServerDateTime(var activity: Activity) {
    var url =
        "https://www.timeapi.io/api/Time/current/coordinate?latitude=20.5937&longitude=78.9629"
    var requestQueue: RequestQueue = Volley.newRequestQueue(activity)

    fun fetch(callBack: CallBack) {
        val json = JSONObject()
        val req = JsonObjectRequest(
            Request.Method.GET, url, json,
            { response ->
                try {
                    callBack.onGetDateTime(response.getString("dateTime"), response.getString("date"))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { }
        requestQueue.add(req)
    }

    interface CallBack {
        fun onGetDateTime(date: String?, time: String?)
    }

}
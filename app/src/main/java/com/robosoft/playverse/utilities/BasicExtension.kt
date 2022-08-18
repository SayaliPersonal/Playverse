package com.robosoft.playverse.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.robosoft.playverse.BuildConfig
import com.robosoft.playverse.R
import org.apache.commons.net.ntp.NTPUDPClient
import java.net.InetAddress
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


// Extension function for fragment to show toast from it.
fun Fragment.toast(str: String?) {
    str ?: return
    context?.toast(str)
}

@BindingAdapter("imageUriCircle")
fun ImageView.imageUriCircle(uri: String?) {
    Glide.with(this)
        .load(uri)
        //.apply(RequestOptions.circleCropTransform())
        .centerCrop()
        .placeholder(R.drawable.bg_avatar_profile)
        //.apply(RequestOptions.circleCropTransform())
        .into(this)
}

fun Context.toast(str: String) {
    applicationContext?.let {
        Log.d("Toast", str)
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(it, str, Toast.LENGTH_SHORT).show()
        }
    }
}
fun Context.isInternetConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

fun ViewPager.autoScroll(interval: Long) {

    val handler = Handler(Looper.getMainLooper())
    var scrollPosition = 0

    val runnable = object : Runnable {

        override fun run() {

            /**
             * Calculate "scroll position" with
             * adapter pages count and current
             * value of scrollPosition.
             */
            val count = adapter?.count ?: 0
            setCurrentItem(scrollPosition++ % count, true)

            handler.postDelayed(this, interval)
        }
    }

    addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            // Updating "scroll position" when user scrolls manually
            scrollPosition = position + 1
        }

        override fun onPageScrollStateChanged(state: Int) {
            // Not necessary
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            // Not necessary
        }
    })

    handler.post(runnable)
}

fun Any.debugLog(str: String) {
    if (BuildConfig.DEBUG) {
        Log.d("MirchiPlayLog | " , str)
    }
}

fun getTotalMillis(startDate: String, endDate: String) : Long{
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    var totalMillis = 0L
    try {
        val dateStart = simpleDateFormat.parse(startDate)
        val dateEnd = simpleDateFormat.parse(endDate)
        val calStart = Calendar.getInstance()
        val calEnd = Calendar.getInstance()
        calStart.time = dateStart
        calEnd.time = dateEnd
        val startMillis: Long =
            calStart.timeInMillis //get the start time in milliseconds

        val endMillis: Long = calEnd.timeInMillis //get the end time in milliseconds

        totalMillis = endMillis - startMillis //total time in milliseconds

    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return totalMillis

}


fun convertCurrency(number: Int): String{
    var numberString = ""
    numberString = if (abs(number / 1000000) > 1) {
        (number / 1000000).toString().toString() + "m"
    } else if (Math.abs(number / 1000) > 1) {
        (number / 1000).toString().toString() + "k"
    } else {
        number.toString()
    }
    return numberString
}



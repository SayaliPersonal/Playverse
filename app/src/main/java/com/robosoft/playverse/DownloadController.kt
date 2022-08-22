package com.robosoft.playverse

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.robosoft.playverse.DownloadController.Companion.destination
import com.robosoft.playverse.DownloadController.Companion.main_context
import com.robosoft.playverse.DownloadController.Companion.progressBar
import com.robosoft.playverse.MainActivity.Companion.REQUEST_INSTALL
import java.io.File


class DownloadController(
    private val context: Context,
    private val url: String,
    public var progressBar1: ProgressBar
) {


    companion object {
            private const val FILE_NAME = "Playverse.apk"
            const val PROVIDER_PATH = ".provider"
         var uri: Uri? = null
         lateinit var destination: String
         lateinit var  main_context: Context
         lateinit var progressBar: ProgressBar
    }



    init {
         main_context=context
         progressBar=progressBar1
    }

        fun enqueueDownload() {
            progressBar.visibility=View.VISIBLE
             destination =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/"
            val fileName = FILE_NAME
            destination += fileName
             uri = Uri.parse("file://$destination")

            val file = File(destination)
            if (file.exists()) //file.delete() - test this, I think sometimes it doesnt work
                file.delete()


            val request = DownloadManager.Request(Uri.parse(url))
            val description =
                request.setDescription("notification_description")
//            request.setMimeType(MIME_TYPE)
            request.setTitle("APK is downloading")
            request.setDescription(context.getString(R.string.downloading))
            request.setDestinationUri(uri)



            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
            val downloadId = manager!!.enqueue(request)

//            showInstallOption()
            val intentFilter = IntentFilter()
            intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
            intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL)
            intentFilter.addDataScheme("package")
            context.registerReceiver(IntentReceiver(), IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            Toast.makeText(context, "downloading", Toast.LENGTH_LONG)
                .show()

        }




    }

class IntentReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        updateApk(this,context)
    }

    private fun updateApk(param: IntentReceiver, context: Context?) {
        progressBar.visibility=View.GONE
        Toast.makeText(context, "completed", Toast.LENGTH_SHORT).show()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val contentUri = FileProvider.getUriForFile(
                context!!,
                BuildConfig.APPLICATION_ID + DownloadController.PROVIDER_PATH,
                File(destination)
            )

            try{

//            Log.d("**rpackageName: ", context.getApplicationInfo().packageName.toString())
//            val install = Intent(Intent.ACTION_VIEW)
//            install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//            install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            install.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//            install.putExtra(Intent.EXTRA_RETURN_RESULT, true);
//            install.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME,
//                context.getApplicationInfo().packageName);
//                install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
//
//            install.setDataAndType(contentUri,"application/vnd.android.package-archive")
////            install.setData(contentUri)
//
//            (main_context as Activity).startActivityForResult(install,REQUEST_INSTALL)
////                context.startActivity(install)
//                context. unregisterReceiver(param)


            }catch (e: Exception){
                Log.d("**rException: ", e.message.toString())

            }

        }
    }

}
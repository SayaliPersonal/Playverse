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
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.robosoft.playverse.DownloadController.Companion.APP_INSTALL_PATH
import com.robosoft.playverse.DownloadController.Companion.destination
import com.robosoft.playverse.DownloadController.Companion.main_context
import com.robosoft.playverse.DownloadController.Companion.progressBar
import com.robosoft.playverse.MainActivity.Companion.REQUEST_INSTALL
import java.io.File


class DownloadController(
    private val context: Context,
    private val url: String,
    public var progressBar1: ProgressBar,
    private val mainLayout: ConstraintLayout
) {


    companion object {
            private const val FILE_NAME = "Playverse.apk"
            const val PROVIDER_PATH = ".provider"
         var uri: Uri? = null
         lateinit var destination: String
         lateinit var  main_context: Context
         lateinit var progressBar: ProgressBar
        const val APP_INSTALL_PATH = "\"application/vnd.android.package-archive\""
    }



    init {
         main_context=context
         progressBar=progressBar1
    }

        fun enqueueDownload() {
            progressBar.visibility=View.VISIBLE
            (main_context as Activity).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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

            context.registerReceiver(IntentReceiver(uri), IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            Snackbar.make(mainLayout, "Downloading", Snackbar.LENGTH_SHORT).show()


        }

    }

class IntentReceiver(private val uri: Uri?) : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        updateApk(this,context,uri)
    }

    private fun updateApk(param: IntentReceiver, context: Context?,  uri: Uri?) {
        progressBar.visibility=View.GONE
        (main_context as Activity).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//        Toast.makeText(context, "completed", Toast.LENGTH_SHORT).show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val contentUri = FileProvider.getUriForFile(
                context!!,
                BuildConfig.APPLICATION_ID + DownloadController.PROVIDER_PATH,
                File(destination)
            )



            try{
                val intentFilter = IntentFilter()
                intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
                intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL)
                intentFilter.addAction(Intent.ACTION_MY_PACKAGE_REPLACED)
                intentFilter.addDataScheme("package")
                context. registerReceiver(InstallsReceiver(), intentFilter)

                val file = File(
                    context!!.getFilesDir(),
                    context!!.getPackageName().toString() + ".apk"
                )
                if (file.exists()) file.delete()

            Log.d("**rpackageName: ", context.getApplicationInfo().packageName.toString())
            Log.d("**rcontentUri: ", contentUri.toString())
                val install = Intent(Intent.ACTION_VIEW)
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
                install.data = contentUri
                context.startActivity(install)

//                (main_context as Activity).startActivityForResult(install,REQUEST_INSTALL)
                (main_context as Activity).finish()
                context.unregisterReceiver(this)


            }catch (e: Exception){
                Log.d("**rException: ", e.message.toString())
            }

        }else
        {
            val install = Intent(Intent.ACTION_VIEW)
            install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            install.setDataAndType(
                this.uri,
                APP_INSTALL_PATH
            )
            context!!.startActivity(install)
            context!!.unregisterReceiver(this)
        }
    }

}


class InstallsReceiver() : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
       Log.d("*******sss","InstallsReceiver")
        val file = File(
            context!!.getFilesDir(),
            context!!.getPackageName().toString() + ".apk"
        )
        if (file.exists()) file.delete()
    }


}
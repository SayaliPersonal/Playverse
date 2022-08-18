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
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.android.exoplayer2.util.MimeTypes
import com.robosoft.playverse.DownloadController.Companion.MIME_TYPE
import com.robosoft.playverse.DownloadController.Companion.destination
import com.robosoft.playverse.DownloadController.Companion.main_context
import com.robosoft.playverse.DownloadController.Companion.uri
import com.robosoft.playverse.MainActivity.Companion.REQUEST_INSTALL
import java.io.File
import java.io.IOException


class DownloadController(private val context: Context, private val url: String) {


    companion object {
            private const val FILE_NAME = "playverse.apk"
            private const val FILE_BASE_PATH = "file://"
        public const val MIME_TYPE = "application/vnd.android.package-archive"
            const val PROVIDER_PATH = ".provider"
            const val APP_INSTALL_PATH = "\"application/vnd.android.package-archive\""
            private const val input_code = "1"
            private const val output_code = "2"
        public var uri: Uri? = null
        public lateinit var destination: String
        public lateinit var  main_context: Context
    }

    init {
         main_context=context
    }

        fun enqueueDownload() {
//   if(input_code< output_code){


             destination =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/"
            val fileName = FILE_NAME
            destination += fileName
             uri = Uri.parse("file://$destination")

            //Delete update file if exists


            Log.d("**r: ",destination)
            //Delete update file if exists
            val file = File(destination)
            if (file.exists()) //file.delete() - test this, I think sometimes it doesnt work
                file.delete()


            //set downloadmanager

            //set downloadmanager
            val request = DownloadManager.Request(Uri.parse(url))
            val description =
                request.setDescription("notification_description")
            request.setMimeType(MIME_TYPE)
            request.setTitle("APK is downloading")
            request.setDescription(context.getString(R.string.downloading))
            //set destination
            request.setDestinationUri(uri)



            // get download service and enqueue file
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
            val downloadId = manager!!.enqueue(request)

            // get download service and enqueue file
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
        Toast.makeText(context, "In", Toast.LENGTH_SHORT).show()

        updateApk(this,context)
    }

    private fun updateApk(param: IntentReceiver, context: Context?) {
        Toast.makeText(context, "completed", Toast.LENGTH_SHORT).show()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val contentUri = FileProvider.getUriForFile(
                context!!,
                BuildConfig.APPLICATION_ID + DownloadController.PROVIDER_PATH,
                File(destination)
            )

            Log.d("**rpackageName: ", context.getApplicationInfo().packageName.toString())
            val install = Intent(Intent.ACTION_VIEW)
            install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            install.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//            install.putExtra(Intent.EXTRA_RETURN_RESULT, true)
//            install.putExtra(Intent.EXTRA_RETURN_RESULT, true);
//            install.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME,
//                context.getApplicationInfo().packageName);

//                install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)

            install.setDataAndType(contentUri, MIME_TYPE)
            context.startActivity(install)

//            (main_context as Activity).startActivityForResult(install,REQUEST_INSTALL)
            context. unregisterReceiver(param)



//            val intent = Intent(Intent.ACTION_INSTALL_PACKAGE)
//            intent.data = getApkUri("playverse.apk")
//            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
//            intent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
//            intent.putExtra(
//                Intent.EXTRA_INSTALLER_PACKAGE_NAME,
//                context.getApplicationInfo().packageName
//            )
//            (main_context as Activity).startActivityForResult(intent, REQUEST_INSTALL)
//
//
//            Log.d("**rd: ", contentUri.toString())
////            (context as Activity).finish()
////            (context as Activity).startActivityForResult(install,1)
//
//        } else {
//            val install = Intent(Intent.ACTION_VIEW)
//            install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//            install.setDataAndType(
//                uri,
//                DownloadController.APP_INSTALL_PATH
//            )
//            context!!.startActivity(install)
//            context!!.unregisterReceiver(param)
//            // finish()
        }
    }


    private fun getApkUri(assetName: String): Uri? {
        val useFileProvider = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        // Copy the given asset out into a file so that it can be installed.
        // Returns the path to the file.
        val tempFilename = "playverse.apk"
        val buffer = ByteArray(16384)
        val fileMode = if (useFileProvider) Context.MODE_PRIVATE else Context.MODE_WORLD_READABLE
        try {
           main_context. getAssets().open(assetName).use { `is` ->
               main_context.openFileOutput(tempFilename, fileMode).use { fout ->
                    var n: Int
                    while (`is`.read(buffer).also { n = it } >= 0) {
                        fout.write(buffer, 0, n)
                    }
                }
            }
        } catch (e: IOException) {
            Log.i("InstallApk", "Failed to write temporary APK file", e)
        }
        return if (useFileProvider) {
            val toInstall: File = File(main_context.getFilesDir(), tempFilename)
            FileProvider.getUriForFile(
                main_context, BuildConfig.APPLICATION_ID + DownloadController.PROVIDER_PATH, toInstall
            )
        } else {
            Uri.fromFile(main_context.getFileStreamPath(tempFilename))
        }
    }



//        private fun showInstallOption(
//
//        ) {
//
//
//
//                // set BroadcastReceiver to install app when .apk is downloaded
//                val onComplete = object : BroadcastReceiver() {
//                    override fun onReceive(
//                        context: Context,
//                        intent: Intent
//                    ) {
//
//                        updateApk( )
//
//                    }
//                }
//                context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
//
//        }

}
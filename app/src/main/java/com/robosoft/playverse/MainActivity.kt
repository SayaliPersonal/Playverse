package com.robosoft.playverse


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.playverse.data.util.Constants
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.databinding.ActivityMainBinding
import com.robosoft.playverse.feature.presentation.view.profile.ServerDateTime
import com.robosoft.playverse.utilities.TemporaryStorage
import dagger.hilt.android.AndroidEntryPoint
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var appUpdateManager: AppUpdateManager? = null
    private val IMMEDIATE_APP_UPDATE_REQ_CODE = 124

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navHostMain) as NavHostFragment).navController
    }
    companion object {
        const val PERMISSION_REQUEST_STORAGE = 0
        const val REQUEST_INSTALL = 1
    }

    lateinit var downloadController: DownloadController
    @Inject
    lateinit var appStorage: AppStorage
    var strApkFileUrl="https://dev-ruskolympus.s3.ap-south-1.amazonaws.com/playverse-apk/playverse.apk"
//    init {
//        System.loadLibrary("native-lib")
//    }
//
//    external fun detectfrida()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpNavigation()
        setUpBottomNavigation()
        // hideSystemBars()

        // This apk is taking pagination sample app
        downloadController = DownloadController(this, strApkFileUrl)

        initRootDetection()
        bottomNavProfileUpdation()
        //downloadAppDialog()
        downloadInstall()
        val time = ServerDateTime(this)
        time.fetch(object : ServerDateTime.CallBack {
            override fun onGetDateTime(date: String?, time: String?) {
                TemporaryStorage.date = date?.substring(0, 10) + " " + date?.substring(11, 19)
            }
        })
    }

    private fun initRootDetection() {
//
//        try {
//            detectfrida()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        val rootBeer = RootBeer(this)
//        GlobalScope.launch(Dispatchers.IO) {
//            if (rootBeer.isRooted) {
//                val builder = MaterialAlertDialogBuilder(this@MainActivity)
//                    .setTitle(resources.getString(R.string.label_root_detect_title))
//                    .setMessage(resources.getString(R.string.label_root_detect_message))
//                    .setCancelable(false)
//                    .setNegativeButton(resources.getString(R.string.label_root_detect_dismiss)) { dialog, _ ->
//                        dialog.dismiss()
//                    }
//
//                withContext(Dispatchers.Main) {
//                    val dialog = builder.create()
//                    dialog.show()
//                }
//            }
//        }
//
        val p: Process
        try {
            // Preform su to get root privledges
            p = Runtime.getRuntime().exec("su")

            // Attempt to write a file to a root-only
            val os = DataOutputStream(p.getOutputStream())
            os.writeBytes("echo \"Do I have root?\" >/system/sd/temporary.txt\n")

            // Close the terminal
            os.writeBytes("exit\n")
            os.flush()
            try {
                p.waitFor()
                if (p.exitValue() !== 255) {
                    Toast.makeText(this, "root", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "not root", Toast.LENGTH_SHORT).show()

                }
            } catch (e: InterruptedException) {
                Toast.makeText(this, "not root", Toast.LENGTH_SHORT).show()

            }
        } catch (e: IOException) {
            Toast.makeText(this, "not root", Toast.LENGTH_SHORT).show()
        }
    }


    private fun downloadInstall() {
        checkStoragePermission()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUEST_INSTALL) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Install succeeded!", Toast.LENGTH_SHORT).show()
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Install canceled!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Install Failed!", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun checkStoragePermission() {
        // Check if the storage permission has been granted
        if (checkSelfPermissionCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // start downloading


            downloadController.enqueueDownload()
//            update(strApkFileUrl)


        } else {
            // Permission is missing and must be requested.
            requestStoragePermission()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // start downloading
                downloadController.enqueueDownload()
            } else {
                // Permission request was denied.
                binding.mainLayout.showSnackbar(R.string.storage_permission_denied, Snackbar.LENGTH_SHORT)
            }
        }
    }
    private fun requestStoragePermission() {

        if (shouldShowRequestPermissionRationaleCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            binding.mainLayout.showSnackbar(
                R.string.storage_access_required,
                Snackbar.LENGTH_INDEFINITE, R.string.ok
            ) {
                requestPermissionsCompat(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_STORAGE
                )
            }

        } else {
            requestPermissionsCompat(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_STORAGE
            )
        }
    }

    private fun redirectStore(strApkFileUrl: String) {



        Log.d("hhhh","hhh "+"redirectStore")
        var  intent= Intent(Intent.ACTION_VIEW, Uri.parse(strApkFileUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(intent)


//        val apkFile =  File(strApkFileUrl);
//        val intent =  Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
//        startActivity(intent);
    }






    private fun setUpNavigation() {
        val graphInflater = navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_main)
        navController.graph = navGraph

        navController.addOnDestinationChangedListener { _, dest, args ->
            onChange(dest, args)
        }
    }

    private fun onChange(dest: NavDestination, args: Bundle?) {
        when (dest.id) {
            R.id.myFundsFragment -> {
                binding.bottomNavigationView.isVisible = true
                binding.bottomNavigationView.menu.findItem(R.id.myFundsFragment).isChecked = true
                bottomNavProfileUpdation()

            }
            R.id.gameDetailsFragment -> {
                binding.bottomNavigationView.isVisible = false

            }
            R.id.settingWebView -> {
                binding.bottomNavigationView.isVisible = false
            }
            R.id.howWorkReferralsFragment -> {
                binding.bottomNavigationView.isVisible = false
            }
            R.id.profileDetailsFragment -> {
                binding.bottomNavigationView.isVisible = true
                binding.bottomNavigationView.menu.findItem(R.id.profileDetailsFragment).isChecked =
                    true
                bottomNavProfileUpdation()
            }
            R.id.withdrawCashFragment -> {
                binding.bottomNavigationView.isVisible = false
            }
            R.id.loginFragment -> {
                binding.bottomNavigationView.isVisible = false
            }
            R.id.loginOtpFragment -> {
                binding.bottomNavigationView.isVisible = false
            }
            R.id.profileCreateFragment -> {
                binding.bottomNavigationView.isVisible = false
            }
            R.id.upiLinkFragment -> {
                binding.bottomNavigationView.isVisible = false
            }
            R.id.homeFragment -> {
                binding.bottomNavigationView.isVisible = true
                binding.bottomNavigationView.menu.findItem(R.id.gameDetailFragment).isChecked = true
                bottomNavProfileUpdation()
            }
            R.id.addCashFragment -> {
                binding.bottomNavigationView.isVisible = false
            }
            R.id.gameFragment -> {
                binding.bottomNavigationView.isVisible = false
            }
            R.id.getStartedFragment -> {
                binding.bottomNavigationView.isVisible = false
            }
            R.id.profileEditFragment -> {
                binding.bottomNavigationView.isVisible = false
            }
            R.id.profileOptionsFragment -> {
                binding.bottomNavigationView.isVisible = false
            }
            R.id.selectAvatarFragment -> {
                binding.bottomNavigationView.isVisible = false
            }
            R.id.setProfilePicFragment -> {
                binding.bottomNavigationView.isVisible = false
            }
            R.id.searchFragment -> {
                binding.bottomNavigationView.isVisible = true
                binding.bottomNavigationView.menu.findItem(R.id.searchFragment).isChecked = true
                bottomNavProfileUpdation()
            }
        }
    }

    private fun setUpBottomNavigation() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profileDetailsFragment -> {
                    if (appStorage.userId.isEmpty()) {
                        findNavController(R.id.navHostMain).navigate(R.id.loginFragment)
                    } else {
                        binding.bottomNavigationView.itemIconTintList = null
                        bottomNavProfileUpdation()
                        findNavController(R.id.navHostMain).navigate(R.id.profileDetailsFragment)
                    }
                }
                R.id.gameDetailFragment -> {
                    binding.bottomNavigationView.itemIconTintList = null
                    findNavController(R.id.navHostMain).navigate(R.id.homeFragment)
                    bottomNavProfileUpdation()
                }
                R.id.searchFragment -> {
                    binding.bottomNavigationView.itemIconTintList = null
                    findNavController(R.id.navHostMain).navigate(R.id.searchFragment)
                    bottomNavProfileUpdation()
                }
                R.id.myFundsFragment -> {
                    binding.bottomNavigationView.itemIconTintList = null
                    if (appStorage.userId.isEmpty()) {
                        findNavController(R.id.navHostMain).navigate(R.id.loginFragment)
                    } else {
                        bottomNavProfileUpdation()
                        findNavController(R.id.navHostMain).navigate(R.id.myFundsFragment)
                    }
                }
            }
            true
        }
    }

    private fun bottomNavProfileUpdation() {
        if (appStorage.profilePic != Constants.DEFAULT_PROFILE_PIC) {
            Glide.with(this)
                .asBitmap()
                .load(appStorage.profilePic)
                .apply(
                    RequestOptions
                        .circleCropTransform()
                        .placeholder(R.drawable.ic_baseline_sentiment_very_satisfied_24)
                )
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        binding.bottomNavigationView.itemIconTintList = null
                        binding.bottomNavigationView.menu.findItem(R.id.profileDetailsFragment).icon =
                            BitmapDrawable(resources, resource)
                    }
                })
        }else{
            binding.bottomNavigationView.menu.findItem(R.id.profileDetailsFragment).setIcon(R.drawable.ic_baseline_sentiment_very_satisfied_24)
        }


    }


}

class UpdateApp : AsyncTask<String?, Void?, Void?>() {
    private var context: Context? = null
    fun setContext(contextf: Context?) {
        context = contextf
    }

    protected override fun doInBackground(vararg params: String?): Void? {
        try {
            val url = URL(params[0])
            val c = url.openConnection() as HttpURLConnection
            c.requestMethod = "GET"
            c.doOutput = true
            c.connect()
            val PATH = "/mnt/sdcard/Download/"
            val file = File(PATH)
            file.mkdirs()
            val outputFile = File(file, "update.apk")
            if (outputFile.exists()) {
                outputFile.delete()
            }
            val fos = FileOutputStream(outputFile)
            val `is` = c.inputStream
            val buffer = ByteArray(1024)
            var len1 = 0
            while (`is`.read(buffer).also { len1 = it } != -1) {
                fos.write(buffer, 0, len1)
            }
            fos.close()
            `is`.close()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(
                Uri.fromFile(File("/mnt/sdcard/Download/update.apk")),
                "application/vnd.android.package-archive"
            )
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK // without this flag android returned a intent error!
            context?.startActivity(intent)
        } catch (e: Exception) {
            Log.e("UpdateAPP", "Update error! " + e.message)
        }
        return null
    }
}

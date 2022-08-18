package com.robosoft.playverse.feature.presentation.viewModel

import android.app.Application
import android.media.Image
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.robosoft.playverse.BuildConfig
import com.robosoft.playverse.utilities.TemporaryStorage
import com.robosoft.playverse.utilities.debugLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    var app: Application,
) : ViewModel() {

    fun saveProfile(image: Int) {
        viewModelScope.launch {
            try {
                val path =
                    "android.resource://" + BuildConfig.APPLICATION_ID.toString() + "/" + image
                val profilePicUri = try {
                    Uri.parse(path)
                } catch (e: Exception) {
                    null
                }



                if (profilePicUri != null) {

                    val requestFile = withContext(Dispatchers.IO) {
                        val tmpFile = File.createTempFile("tmp_file_img", ".png").apply {
                            createNewFile()
                            deleteOnExit()
                        }

                        app.contentResolver.openInputStream(profilePicUri).let {
                            it?.copyTo(tmpFile.outputStream())
                        }

                        tmpFile.asRequestBody()

                    }

                    val body = MultipartBody.Part.createFormData(
                        "image",
                        "f_" + System.currentTimeMillis() + ".jpeg",
                        requestFile
                    )

                    TemporaryStorage.image = body
                }

            } catch (e: Exception) {
                e.printStackTrace()
                debugLog("exceptionnnnnnn is $e")

            }
        }
    }
}
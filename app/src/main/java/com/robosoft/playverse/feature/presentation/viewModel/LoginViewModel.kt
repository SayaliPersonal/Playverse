package com.robosoft.playverse.feature.presentation.viewModel


import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.playverse.data.api.ApiHelper
import com.playverse.data.models.AvatarResponse
import com.playverse.data.models.MainOtpResponse
import com.playverse.data.repository.MainRepository
import com.playverse.data.util.Result
import com.playverse.data.util.ServerError
import com.robosoft.playverse.BuildConfig
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.base.BaseViewModel
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
class LoginViewModel @Inject constructor(
    var repo: MainRepository,
    var app: Application,
    apiHelper: ApiHelper,
    var appStorage: AppStorage
) : BaseViewModel(apiHelper) {

    var profilePicUri: MutableLiveData<String> = MutableLiveData()


    private var _getOtp: MutableLiveData<MainOtpResponse> = MutableLiveData()
    val getOtp: LiveData<MainOtpResponse>
        get() = _getOtp

    private var _signUp: MutableLiveData<MainOtpResponse> = MutableLiveData()
    val signUp: LiveData<MainOtpResponse>
        get() = _signUp

    private var _verifyOtp: MutableLiveData<MainOtpResponse> = MutableLiveData()
    val verifyOtp: LiveData<MainOtpResponse>
        get() = _verifyOtp


    private var _failure: MutableLiveData<ServerError> = MutableLiveData()
    val failure: LiveData<ServerError>
        get() = _failure

    private var _loginFailure: MutableLiveData<ServerError> = MutableLiveData()
    val loginFailure: LiveData<ServerError>
        get() = _loginFailure

    private var _failureUsername: MutableLiveData<ServerError> = MutableLiveData()
    val failureUsername: LiveData<ServerError>
        get() = _failureUsername

    private var _verifyUsername: MutableLiveData<AvatarResponse.MainResponse> = MutableLiveData()
    val verifyUsername: LiveData<AvatarResponse.MainResponse>
        get() = _verifyUsername


    fun setUpAvatarUsername(
        image: MultipartBody.Part,
        userId: Int,
        username: String
    ): MutableLiveData<AvatarResponse.MainResponse> {
        viewModelScope.launch {
            repo.setUpAvatarUsername(image, userId, username).collect {
                when (it.status) {
                    Result.Status.LOADING -> {
                        isLoadingData.postValue(true)
                    }
                    Result.Status.SUCCESS -> {
                        isLoadingData.postValue(false)
                        _verifyUsername.postValue(((it as Result.Success).data))
                    }
                    Result.Status.FAILURE -> {
                        isLoadingData.postValue(false)

                        debugLog("failure is ${Gson().toJson((it as Result.Failure).errorResponse)}")
                        _failureUsername.postValue(((it as Result.Failure).errorResponse))
                    }
                }
            }
        }
        return _verifyUsername
    }

    fun verifyOtpNew(number: String, otp: String, type: String): MutableLiveData<MainOtpResponse> {
        viewModelScope.launch {
            repo.verifyOtp(number, otp, type).collect {
                when (it.status) {
                    Result.Status.LOADING -> {
                        isLoadingData.postValue(true)
                    }
                    Result.Status.SUCCESS -> {
                        isLoadingData.postValue(false)
                        _verifyOtp.postValue(((it as Result.Success).data))
                    }
                    Result.Status.FAILURE -> {
                        isLoadingData.postValue(false)
                        _failure.postValue(((it as Result.Failure).errorResponse))
                        /*(it as Result.Failure).errorResponse.throwable?.let { it1 -> onLoadFail(it1) }!!*/
                    }
                }
            }
        }
        return _verifyOtp

    }

    fun updateProfilePic(uri: Uri) {
        profilePicUri.postValue(uri.toString())
    }

    fun login(number: String): MutableLiveData<MainOtpResponse> {
        viewModelScope.launch {
            repo.login(number).collect {
                when (it.status) {
                    Result.Status.LOADING -> {
                        isLoadingData.postValue(true)
                    }
                    Result.Status.SUCCESS -> {
                        isLoadingData.postValue(false)
                        _getOtp.postValue(((it as Result.Success).data))
                    }
                    Result.Status.FAILURE -> {
                        isLoadingData.postValue(false)
                    }
                }
            }
        }
        return _getOtp

    }


    fun signUp(number: String): MutableLiveData<MainOtpResponse> {
        viewModelScope.launch {
            repo.signUp(number).collect {
                when (it.status) {
                    Result.Status.LOADING -> {
                        isLoadingData.postValue(true)
                    }
                    Result.Status.SUCCESS -> {
                        isLoadingData.postValue(false)
                        _signUp.postValue(((it as Result.Success).data))
                    }
                    Result.Status.FAILURE -> {
                        isLoadingData.postValue(false)
                        _loginFailure.postValue(((it as Result.Failure).errorResponse))
                    }
                }
            }
        }
        return _signUp

    }

    fun saveProfile() {
        viewModelScope.launch {
            try {
                val profilePicUri = try {
                    Uri.parse(profilePicUri.value)
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
                    debugLog("body is $body")



                }

            } catch (e: Exception) {
                e.printStackTrace()
                debugLog("exception is $e")

            }
        }
    }

    fun saveProfileNew() {
        viewModelScope.launch {
            try {
                val path =
                    "android.resource://" + BuildConfig.APPLICATION_ID.toString() + "/" + com.robosoft.playverse.R.raw.img
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

    fun updateProfileName(
        userId: Int,
        username: String
    ): MutableLiveData<AvatarResponse.MainResponse> {
        viewModelScope.launch {
            repo.updateProfileName( userId, username).collect {
                when (it.status) {
                    Result.Status.LOADING -> {
                        isLoadingData.postValue(true)
                    }
                    Result.Status.SUCCESS -> {
                        isLoadingData.postValue(false)
                        _verifyUsername.postValue(((it as Result.Success).data))
                    }
                    Result.Status.FAILURE -> {
                        isLoadingData.postValue(false)
                        _failureUsername.postValue(((it as Result.Failure).errorResponse))
                    }
                }
            }
        }
        return _verifyUsername
    }

    fun updateProfilePic(
        image: MultipartBody.Part,
        user_id: Int,
    ): MutableLiveData<AvatarResponse.MainResponse> {
        viewModelScope.launch {
            repo.updateProfilePic( image,user_id).collect {
                when (it.status) {
                    Result.Status.LOADING -> {
                        isLoadingData.postValue(true)
                    }
                    Result.Status.SUCCESS -> {
                        isLoadingData.postValue(false)
                        _verifyUsername.postValue(((it as Result.Success).data))
                    }
                    Result.Status.FAILURE -> {
                        isLoadingData.postValue(false)
                        _failureUsername.postValue(((it as Result.Failure).errorResponse))
                    }
                }
            }
        }
        return _verifyUsername
    }
}
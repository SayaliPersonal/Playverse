package com.robosoft.playverse.feature.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.playverse.data.api.ApiHelper
import com.playverse.data.models.LogoutRequestBody
import com.playverse.data.models.LogoutResponse
import com.playverse.data.repository.MainRepository
import com.playverse.data.util.Result
import com.robosoft.playverse.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    apiHelper: ApiHelper
) : BaseViewModel(
    apiHelper = apiHelper
) {
    private val _logoutResponse = MutableLiveData<LogoutResponse>()

    val logoutResponse: LiveData<LogoutResponse> = _logoutResponse


    fun logout(accessToken: String) {
        viewModelScope.launch {
            mainRepository.logout(LogoutRequestBody(accessToken = accessToken))?.collect {
                when (it.status) {
                    Result.Status.LOADING -> {
                        isLoadingData.postValue(true)
                    }
                    Result.Status.SUCCESS -> {
                        isLoadingData.postValue(false)
                        _logoutResponse.postValue(((it as Result.Success).data))
                    }
                    Result.Status.FAILURE -> {
                        isLoadingData.postValue(false)
                    }
                }

            }
        }
    }
}
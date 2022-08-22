package com.robosoft.playverse.feature.presentation.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.playverse.data.api.ApiHelper
import com.playverse.data.api.ApiService
import com.playverse.data.models.*
import com.playverse.data.repository.MainRepository
import com.playverse.data.util.Result
import com.robosoft.playverse.base.BaseViewModel
import com.robosoft.playverse.feature.presentation.view.home.TournamentDataCell
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VesrionViewModel  @Inject constructor(
    apiHelper: ApiHelper,
   var  apiService: ApiService
) : BaseViewModel(apiHelper) {

    private val _res = MutableLiveData<VersionUpdate>()

    val res: LiveData<VersionUpdate>
        get() = _res

    fun getVersionDetails() = liveData {
        emit(apiService.versionDetails())
    }


//    fun getVersionDetails() = viewModelScope.launch {
//        apiService.versionDetails()?.collect {
//            when (it.status) {
//                Result.Status.SUCCESS -> {
//                    isLoadingData.postValue(false)
//                    _res.postValue(((it as Result.Success).data))
//                }
//                Result.Status.FAILURE -> {
//                    isLoadingData.postValue(false)
//                    (it as Result.Failure).errorResponse.throwable?.let { it1 -> onLoadFail(it1) }
//                }
//
//                else -> {
//                    // do nothing
//                }
//            }
//        }
//    }



}
package com.robosoft.playverse.feature.presentation.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.playverse.data.api.ApiHelper
import com.playverse.data.models.AddCashRequestBody
import com.playverse.data.models.AddCashResponse
import com.playverse.data.repository.MainRepository
import com.playverse.data.util.Result
import com.playverse.data.util.ServerError
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddCashViewModel @Inject constructor(
    var appStorage: AppStorage,
    var repo: MainRepository,
    apiHelper: ApiHelper
) : BaseViewModel(apiHelper) {

    private var _addCash: MutableLiveData<AddCashResponse> = MutableLiveData()
    val addCash: LiveData<AddCashResponse>
        get() = _addCash

    private var _addCashFailure: MutableLiveData<ServerError> = MutableLiveData()
    val addCashFailure: LiveData<ServerError>
        get() = _addCashFailure


    fun addCashBalance(
        amount: Int,
        userId: Int
    ): MutableLiveData<AddCashResponse> {
        viewModelScope.launch {
            repo.addCashBalance(
                AddCashRequestBody(
                 amount, userId
            )).collect{
                when (it.status) {
                    Result.Status.LOADING -> {
                        isLoadingData.postValue(true)
                    }
                    Result.Status.SUCCESS -> {
                        isLoadingData.postValue(false)
                        _addCash.postValue(((it as Result.Success).data))
                    }
                    Result.Status.FAILURE -> {
                        isLoadingData.postValue(false)
                        _addCashFailure.postValue(((it as Result.Failure).errorResponse))
                    }
                }

            }
        }
        return _addCash
    }

}
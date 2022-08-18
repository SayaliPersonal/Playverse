package com.robosoft.playverse.feature.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.playverse.data.api.ApiHelper
import com.playverse.data.models.WithdrawCashRequestBody
import com.playverse.data.models.WithdrawCashResponse
import com.playverse.data.repository.MainRepository
import com.playverse.data.util.Result
import com.playverse.data.util.ServerError
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithdrawCahViewModel @Inject constructor(
    var appStorage: AppStorage,
    var repo: MainRepository,
    apiHelper: ApiHelper
) : BaseViewModel(apiHelper) {

    private var _withdrawCash: MutableLiveData<WithdrawCashResponse> = MutableLiveData()
    val withdrawCash: LiveData<WithdrawCashResponse>
        get() = _withdrawCash

    private var _withdrawFailure: MutableLiveData<ServerError> = MutableLiveData()
    val withdrawFailure: LiveData<ServerError>
        get() = _withdrawFailure

    fun withdrawCash(
        amount: Int,
        userId: Int
    ): MutableLiveData<WithdrawCashResponse> {
        viewModelScope.launch {
            repo.withdrawCash(
                WithdrawCashRequestBody(
                    amount,
                    userId
                )
            ).collect{
                when (it.status) {
                    Result.Status.LOADING -> {
                        isLoadingData.postValue(true)
                    }
                    Result.Status.SUCCESS -> {
                        isLoadingData.postValue(false)
                        _withdrawCash.postValue(((it as Result.Success).data))
                    }
                    Result.Status.FAILURE -> {
                        _withdrawFailure.postValue(((it as Result.Failure).errorResponse))
                    }
                }

            }
        }
        return _withdrawCash
    }

}
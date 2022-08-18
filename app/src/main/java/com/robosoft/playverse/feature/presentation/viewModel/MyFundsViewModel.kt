package com.robosoft.playverse.feature.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playverse.data.api.ApiHelper
import com.playverse.data.models.BaseApiResponse
import com.playverse.data.models.MyFundsResponse
import com.playverse.data.models.ProfileMainResponse
import com.playverse.data.repository.MainRepository
import com.playverse.data.util.Result
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyFundsViewModel @Inject constructor(
    var appStorage: AppStorage,
    var repo: MainRepository,
    apiHelper: ApiHelper
) : BaseViewModel(apiHelper) {

    private var _cashBalance: MutableLiveData<BaseApiResponse<MyFundsResponse>> = MutableLiveData()
    val cashBalance: LiveData<BaseApiResponse<MyFundsResponse>>
        get() = _cashBalance

    init {
        fetchCashBalance()
    }

    private fun fetchCashBalance(): MutableLiveData<BaseApiResponse<MyFundsResponse>> {
        viewModelScope.launch {
            repo.fetchCashBalance(appStorage.userId.toInt()).collect{
                when (it.status) {
                    Result.Status.LOADING -> {
                        isLoadingData.postValue(true)
                    }
                    Result.Status.SUCCESS -> {
                        isLoadingData.postValue(false)
                        _cashBalance.postValue(((it as Result.Success).data))
                    }
                    Result.Status.FAILURE -> {
                        isLoadingData.postValue(false)
                    }
                }

            }
        }
        return _cashBalance
    }

}
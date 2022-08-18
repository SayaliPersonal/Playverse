package com.robosoft.playverse.feature.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.playverse.data.api.ApiHelper
import com.playverse.data.models.DepositListingResponse
import com.playverse.data.repository.MainRepository
import com.playverse.data.util.Result
import com.robosoft.playverse.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepositGameListingViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    apiHelper: ApiHelper
) : BaseViewModel(apiHelper) {

    private val _res = MutableLiveData<DepositListingResponse>()

    val res: LiveData<DepositListingResponse> = _res

    fun getDepositGameListing(status: String, size: String) = viewModelScope.launch {
        mainRepository.getDepositGameListing(size = size, status = status)?.collect {
            when (it.status) {
                Result.Status.LOADING -> {
                    isLoadingData.postValue(true)
                }
                Result.Status.SUCCESS -> {
                    isLoadingData.postValue(false)
                    _res.postValue(((it as Result.Success).data))
                }
                Result.Status.FAILURE -> {
                    isLoadingData.postValue(false)
                    (it as Result.Failure).errorResponse.throwable?.let { it1 -> onLoadFail(it1) }
                }

                else -> {
                    // do nothing
                }
            }
        }
    }
}

package com.robosoft.playverse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.playverse.data.api.ApiHelper
import com.playverse.data.models.GameBannerAndInfoResponse
import com.playverse.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.playverse.data.util.Result
import com.robosoft.playverse.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    apiHelper: ApiHelper
) : BaseViewModel(apiHelper) {



}

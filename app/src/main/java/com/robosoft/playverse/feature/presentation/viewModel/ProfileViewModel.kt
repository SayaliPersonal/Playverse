package com.robosoft.playverse.feature.presentation.viewModel

import android.app.Application
import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.playverse.data.api.ApiHelper
import com.playverse.data.models.BaseApiResponse
import com.playverse.data.models.MainOtpResponse
import com.playverse.data.models.ProfileMainResponse
import com.playverse.data.models.ProfileTournamentResponse
import com.playverse.data.repository.MainRepository
import com.playverse.data.util.Result
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.base.BaseViewModel
import com.robosoft.playverse.utilities.SessionClass
import com.robosoft.playverse.utilities.debugLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    var repo : MainRepository,
    var app: Application,
    var appStorage: AppStorage,
    apiHelper: ApiHelper
) : BaseViewModel(apiHelper) {


    private var _fetch: MutableLiveData<ProfileMainResponse> = MutableLiveData()
    val fetch: LiveData<ProfileMainResponse>
        get() = _fetch

    private var _profileTournaments: MutableLiveData<BaseApiResponse<List<ProfileTournamentResponse>>> = MutableLiveData()
    val profileTournaments: LiveData<BaseApiResponse<List<ProfileTournamentResponse>>>
        get() = _profileTournaments

    init {
        fetchUserDetails()
        fetchProfileTournaments()
    }


    private fun fetchUserDetails(): MutableLiveData<ProfileMainResponse> {
        viewModelScope.launch {
            repo.fetchUserDetails(appStorage.userId.toInt()).collect{
                when (it.status) {
                        Result.Status.LOADING -> {
                            isLoadingData.postValue(true)
                        }
                        Result.Status.SUCCESS -> {
                            isLoadingData.postValue(false)
                            _fetch.postValue(((it as Result.Success).data))
                        }
                        Result.Status.FAILURE -> {
                            isLoadingData.postValue(false)
                        }
                    }

            }
        }
        return _fetch
    }

    private fun fetchProfileTournaments(): MutableLiveData<BaseApiResponse<List<ProfileTournamentResponse>>> {
        viewModelScope.launch {
            repo.fetchProfileTournaments(appStorage.userId.toInt()).collect{

                    when (it.status) {
                        Result.Status.LOADING -> {
                            isLoadingData.postValue(true)
                        }
                        Result.Status.SUCCESS -> {
                            isLoadingData.postValue(false)
                            _profileTournaments.postValue(((it as Result.Success).data))
                        }
                        Result.Status.FAILURE -> {
                            isLoadingData.postValue(false)
                        }
                    }

            }
        }
        return _profileTournaments
    }

    fun fetchSessionIdForGame(
        context: Context,
        gameId: String,
        userId: String,
        gameModeType: String,
        gameModeId: String
    ) {
        fetchSessionId(
            context = context,
            gameId = gameId,
            userId = userId,
            gameModeType = gameModeType,
            gameModeId = gameModeId
        )
    }
}

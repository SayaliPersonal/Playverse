package com.robosoft.playverse.feature.presentation.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.playverse.data.api.ApiHelper
import com.playverse.data.models.GameBannerAndInfoResponse
import com.playverse.data.models.RecentGameResponse
import com.playverse.data.repository.MainRepository
import com.playverse.data.util.Result
import com.robosoft.playverse.base.BaseViewModel
import com.robosoft.playverse.utilities.debugLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    apiHelper: ApiHelper
) : BaseViewModel(apiHelper) {

    private val _res = MutableLiveData<GameBannerAndInfoResponse>()

    val res: LiveData<GameBannerAndInfoResponse> = _res

    private val _recentGames = MutableLiveData<RecentGameResponse>()
    val recentGames: LiveData<RecentGameResponse> = _recentGames


    fun getBannerAndOtherInfo(gameId: String) = viewModelScope.launch {
        mainRepository.getBannerInfo(gameId = gameId)?.collect {
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

    fun getRecentGames(gameId: String, userId: String) {
        viewModelScope.launch {
            mainRepository.getRecentGames(gameId = gameId, userId = userId)?.collect {
                when (it.status) {
                    Result.Status.LOADING -> {
                        isLoadingData.postValue(true)
                    }
                    Result.Status.SUCCESS -> {
                        isLoadingData.postValue(false)
                        debugLog("data is ${Gson().toJson(it)}")
                        _recentGames.postValue(((it as Result.Success).data))
                    }
                    Result.Status.FAILURE -> {
                        debugLog("data is failure")
                        isLoadingData.postValue(false)
                    }
                }

            }
        }
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
    fun buyInPopUpDetails(
        context: Context,
        gameId: Int,
        entreFee: Int,
        tournamentId: Int,
        userId: Int
    ) {
        buyInPopUP(
            context = context,
            gameId = gameId,
            entreFee = entreFee,
            tournamentId = tournamentId,
            userId = userId
        )
    }

    fun registerationDetails(
        context: Context,
        gameId: Int,
        tournamentId: Int,
        userId: Int
    ) {
        tournamentReristeration(
            context = context,
            gameId = gameId,
            tournamentId = tournamentId,
            userId = userId
        )
    }

    /*fun getRecentGames(gameId: String, userId: String) = viewModelScope.launch {
        mainRepository.getRecentGames(gameId = gameId, userId = userId)?.collect {
            when (it.status) {
                Result.Status.LOADING -> {
                    isLoadingData.postValue(true)
                }
                Result.Status.SUCCESS -> {
                    isLoadingData.postValue(false)
                    _recentGames.postValue(((it as Result.Success).data))
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
    }*/
}

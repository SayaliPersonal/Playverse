package com.robosoft.playverse.feature.presentation.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.playverse.data.api.ApiHelper
import com.playverse.data.models.LeaderAndRewardsResponse
import com.playverse.data.models.RecentGameResponse
import com.playverse.data.repository.MainRepository
import com.playverse.data.util.Result
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameRewardsAndLeaderBoardViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    apiHelper: ApiHelper,
    private val appStorage: AppStorage
) : BaseViewModel(apiHelper) {

    private val _rewards = MutableLiveData<LeaderAndRewardsResponse>()
    val rewards: LiveData<LeaderAndRewardsResponse> = _rewards


    fun getRewardsAndLeaderBoard(gameId: String) = viewModelScope.launch {
        mainRepository.getRewardsAndLeaderBoard(gameId = gameId, user_id = appStorage.userId)?.collect {
            when (it.status) {
                Result.Status.LOADING -> {
                    isLoadingData.postValue(true)
                }
                Result.Status.SUCCESS -> {
                    isLoadingData.postValue(false)
                    _rewards.postValue(((it as Result.Success).data))
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
}

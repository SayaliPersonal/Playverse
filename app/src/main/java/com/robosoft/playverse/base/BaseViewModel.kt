package com.robosoft.playverse.base

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playverse.data.api.ApiHelper
import com.playverse.data.models.*
import com.playverse.data.util.Result
import com.playverse.data.util.ServerError
import kotlinx.coroutines.*


abstract class BaseViewModel(private val apiHelper: ApiHelper) : ViewModel() {

    protected val isLoadingData = MutableLiveData<Boolean>().apply { value = false }
    val isLoading: LiveData<Boolean> = isLoadingData

    private val _sessionId = MutableLiveData<SessionIdResponse>()
    val sessionId: LiveData<SessionIdResponse> = _sessionId

    private val _buyIn = MutableLiveData<TournamentBuyInResponse>()
    val buyIn: LiveData<TournamentBuyInResponse> = _buyIn

    private val _registeration = MutableLiveData<TournamentRegistered>()
    val registeration: LiveData<TournamentRegistered> = _registeration

    /**
     * handle throwable error
     */
    open suspend fun onLoadFail(throwable: Throwable) {
        withContext(Dispatchers.Main) {

        }
    }

    // game mode type -  1 - for tournament,  2- free game play
    @OptIn(DelicateCoroutinesApi::class)
    fun fetchSessionId(
        context: Context,
        gameId: String,
        userId: String,
        gameModeType: String,
        gameModeId: String
    ) = GlobalScope.launch {
        apiHelper.fetchSessionId(
            context = context,
            SessionRequestBody(
                game_id = gameId,
                user_id = userId,
                game_mode_type = gameModeType,
                game_mode_id = gameModeId
            )
        )?.collect {
            when (it.status) {
                Result.Status.LOADING -> {
                    isLoadingData.postValue(true)
                }
                Result.Status.SUCCESS -> {
                    isLoadingData.postValue(false)
                    _sessionId.postValue(((it as Result.Success).data))
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

    @OptIn(DelicateCoroutinesApi::class)
    fun buyInPopUP(
        context: Context,
        gameId: Int,
        entreFee: Int,
        tournamentId: Int,
        userId: Int
    ) = GlobalScope.launch {
        apiHelper.tournamentBuyIn(
            context = context,
            BuyinPopUpResponse(
                entry_fee = entreFee,
                game_id = gameId,
                tournament_id = tournamentId,
                user_id = userId
            )
        )?.collect {
            when (it.status) {
                Result.Status.LOADING -> {
                    isLoadingData.postValue(true)
                }
                Result.Status.SUCCESS -> {
                    isLoadingData.postValue(false)
                    _buyIn.postValue(((it as Result.Success).data))
                }
                Result.Status.FAILURE -> {
                    isLoadingData.postValue(false)
                    (it as Result.Failure).errorResponse.throwable?.let { it1 -> onLoadFail(it1) }
                }
            }
        }

    }


    @OptIn(DelicateCoroutinesApi::class)
    fun tournamentReristeration(
        context: Context,
        gameId: Int,
        tournamentId: Int,
        userId: Int
    ) = GlobalScope.launch {
        apiHelper.tournamentRegisteration(
            context = context,
                gameId,
                tournamentId,
                userId
        )?.collect {
            when (it.status) {
                Result.Status.LOADING -> {
                    isLoadingData.postValue(true)
                }
                Result.Status.SUCCESS -> {
                    isLoadingData.postValue(false)
                    _registeration.postValue(((it as Result.Success).data))
                }
                Result.Status.FAILURE -> {
                    isLoadingData.postValue(false)
                    (it as Result.Failure).errorResponse.throwable?.let { it1 -> onLoadFail(it1) }
                }
            }
        }

    }

}


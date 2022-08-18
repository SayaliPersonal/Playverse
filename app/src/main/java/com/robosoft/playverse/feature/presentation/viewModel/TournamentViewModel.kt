package com.robosoft.playverse.feature.presentation.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.playverse.data.api.ApiHelper
import com.playverse.data.models.*
import com.playverse.data.repository.MainRepository
import com.playverse.data.util.Result
import com.robosoft.playverse.base.BaseViewModel
import com.robosoft.playverse.feature.presentation.view.home.TournamentDataCell
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TournamentViewModel  @Inject constructor(
    private val mainRepository: MainRepository,
    apiHelper: ApiHelper
) : BaseViewModel(apiHelper) {

    private val _res = MutableLiveData<TournamentListingResponse>()

    val res: LiveData<TournamentListingResponse>
        get() = _res


   /* init {
        getTournamentList()
    }*/

    fun getTournamentList(page : Int, size : Int) = viewModelScope.launch {
        mainRepository.getTournamentListInfo(page ,size,4, "desc")?.collect {
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

    fun createCellsFromList(data: TournamentListingResponse): ArrayList<TournamentDataCell> {
        val dataCells: ArrayList<TournamentDataCell> = arrayListOf()
        data.data.tournaments.forEach {
            dataCells.add(
                TournamentDataCell(
                    buyIn = it.buyIn ?: 0,
                    status = it.status?: "",
                    takeRate = it.takeRate?: 0,
                    frequency = it.frequency ?: "",
                    fromDate = it.fromDate?: "",
                    toDate = it.toDate?: "",
                    game = it.game?: "",
                    gameId = it.gameId?: 0,
                    gameLink = it.gameLink?: "",
                    gamePlays = it.gamePlays?: 0,
                    gameIcon = it.gameIcon?: "",
                    id = it.id?: 0,
                    rewardsWon = it.rewardsWon?: 0,
                    userCount = it.userCount?: 0,
                    videoUrl = it.videoUrl?: "",
                    category = it.category?: "",
                    rewards = it.rewards?:0
                )
            )

        }

        return dataCells
    }
}
package com.robosoft.playverse.feature.presentation.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.playverse.data.api.ApiHelper
import com.playverse.data.models.FreePlayListingResponse
import com.playverse.data.repository.MainRepository
import com.playverse.data.util.Result
import com.robosoft.playverse.base.BaseViewModel
import com.robosoft.playverse.feature.presentation.view.home.FreePlayCell
import com.robosoft.playverse.feature.presentation.view.home.VideoUrlCell
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FreePlayViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    apiHelper: ApiHelper
) : BaseViewModel(apiHelper) {

    private val _res = MutableLiveData<FreePlayListingResponse>()

    val res: LiveData<FreePlayListingResponse>
        get() = _res

    /*init {
        getFreePlayList()
    }*/

    fun getFreePlayList(page : Int, size : Int) = viewModelScope.launch {
        mainRepository.getFreePlayListInfo(page ,size )?.collect {
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

    fun createCellsFromList(data: FreePlayListingResponse): ArrayList<FreePlayCell> {
        val dataCells: ArrayList<FreePlayCell> = arrayListOf()
        data.data.freeGameplay.forEach {
            dataCells.add( FreePlayCell(
                description = it.description,
                gameIcon = it.gameIcon,
                gameLink = it.gameLink,
                gamePlays = it.gamePlays,
                id=it.id,
                rewards = it.rewards,
                score=it.score,
                userCount = it.userCount,
                videoUrl = it.videoUrl,
                game = it.game,
                gameId = it.gameId
            )
            )
        }
        return dataCells
    }

    fun videoFromListdata(data:  FreePlayListingResponse): ArrayList<VideoUrlCell> {
        val videoCell: ArrayList<VideoUrlCell> = arrayListOf()
        data.data.freeGameplay.forEach {
            videoCell.add( VideoUrlCell(
                videoUrl = it.videoUrl
                )
            )
        }
        return videoCell
    }
}
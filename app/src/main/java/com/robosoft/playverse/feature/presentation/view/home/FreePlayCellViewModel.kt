package com.robosoft.playverse.feature.presentation.view.home

import androidx.lifecycle.ViewModel
import com.playverse.data.models.FreeGameplay

class FreePlayCellViewModel(val data: FreePlayCell) : ViewModel() {
    fun FreeGameplay(): FreeGameplay {
        return FreeGameplay(
            description = data.description,
            gameIcon = data.gameIcon,
            gameLink = data.gameLink,
            gamePlays = data.gamePlays,
            rewards = data.rewards,
            userCount = data.userCount,
            videoUrl = data.videoUrl,
            gameId = data.gameId,
            game = data.game,
            score = data.score,
            id = data.id
        )
    }
}
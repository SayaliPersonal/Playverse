package com.robosoft.playverse.feature.presentation.view.home

import com.playverse.data.models.FreeGameplay

interface FreeGamePlayClickListener {
    fun getFreePlayData(position: Int, data: FreeGameplay)
}
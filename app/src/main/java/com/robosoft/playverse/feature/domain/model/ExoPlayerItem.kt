package com.robosoft.playverse.feature.domain.model

import com.google.android.exoplayer2.ExoPlayer

class ExoPlayerItem(
    var exoPlayer: ExoPlayer,
    var position: Int = 0
)
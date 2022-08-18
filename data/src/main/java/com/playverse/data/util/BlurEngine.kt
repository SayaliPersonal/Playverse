package com.playverse.data.util

import android.graphics.Bitmap

interface BlurEngine {
    fun blur(image: Bitmap, radius: Int): Bitmap
    fun getType(): String
}
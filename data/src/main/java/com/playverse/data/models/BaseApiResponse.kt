package com.playverse.data.models

import com.google.gson.annotations.SerializedName

class BaseApiResponse<T>(
    @field:SerializedName("code") var code: Int? = null,
    @field:SerializedName("success") var success: Boolean,
    @field:SerializedName("data") var data: T? = null,

    ) {

}
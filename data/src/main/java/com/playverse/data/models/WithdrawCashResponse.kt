package com.playverse.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class WithdrawCashResponse(

    @field:SerializedName("code")
    val code: Int= 0,

    @field:SerializedName("data")
    val data: WithdrawData = WithdrawData(),

    @field:SerializedName("success")
    val success: Boolean = false
) : Parcelable


@Parcelize
data class WithdrawData(

    @field:SerializedName("cash_withdrawn")
    val cashWithdrawn: Int = 0,

    @field:SerializedName("processing_fee")
    val processingFee: Int = 0,

    @field:SerializedName("tds")
    val tds: Double = 0.0,

    @field:SerializedName("amount_received")
    val amountReceived: Double = 0.0,

    @field:SerializedName("withdrawal_id")
    val withdrawalId: String = "",

    @field:SerializedName("withdrawal_status")
    val withdrawalStatus: String = ""

) : Parcelable

data class WithdrawCashRequestBody(
    var amount: Int,
    var user_id: Int

)

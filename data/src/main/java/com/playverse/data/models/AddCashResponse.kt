package com.playverse.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddCashResponse(

    @field:SerializedName("code")
    val code: Int= 0,

    @field:SerializedName("data")
    val data: CashData = CashData(),

    @field:SerializedName("success")
    val success: Boolean = false
) : Parcelable


@Parcelize
data class CashData(

    @field:SerializedName("cash_added")
    val cashAdded: Int =0,

    @field:SerializedName("payment_status")
    val paymentStatus: String = "",

    @field:SerializedName("payment_time")
    val paymentTime: String = "",

    @field:SerializedName("transaction_id")
    val transactionId: String = "",

    @field:SerializedName("user_id")
    val userId: Int = 0,

    @field:SerializedName("wallet_balance")
    val walletBalance: Int = 0

) : Parcelable

data class AddCashRequestBody(
    var amount: Int,
    var user_id: Int

)
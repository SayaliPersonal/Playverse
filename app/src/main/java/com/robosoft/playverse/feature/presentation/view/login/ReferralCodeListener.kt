package com.robosoft.playverse.feature.presentation.view.login

interface ReferralCodeListener {

    fun getReferralCode(amount: String, isSuccess: Boolean)
    fun getToastMsg(isShow: Boolean)
}
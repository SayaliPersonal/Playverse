package com.robosoft.playverse.utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import com.playverse.data.util.Constants


class OTPReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        val smsMessages: Array<SmsMessage> = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (smsMessage in smsMessages) {
            val messageBody: String = smsMessage.messageBody
            debugLog("addrress is ${smsMessage.originatingAddress}")
            if(smsMessage.originatingAddress == Constants.ADDRESS){
                debugLog("correct")
                val otp = messageBody.substring(0,6)
                TemporaryStorage.otp = otp
                TemporaryStorage.check.value = otp
            }
        }
    }

}
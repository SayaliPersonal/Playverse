package com.robosoft.playverse.utilities

import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged


class OtpManager(
    private var otpInputType: Int = InputType.TYPE_NUMBER_VARIATION_PASSWORD,
    private vararg val editTexts: EditText,
    private val onOtpChange: (otp: String) -> Unit
) {

    init {
        setupListeners()
    }

    private fun setupListeners() {
        for (i in editTexts.indices) {
            val editText = editTexts[i]



            editText.doAfterTextChanged {
                if (it?.length == 1 && i < editTexts.size - 1) {
                    editTexts[i + 1].requestFocus()
                } else if (it?.length == 0 && i > 0) {
                    editTexts[i - 1].requestFocus()
                }
                val otp = getTextOtp()
                onOtpChange(otp)
            }

            editText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                if (i > 0 && hasFocus && editTexts[i - 1].text.isEmpty()) {
                    editTexts[i - 1].requestFocus()

                }
            }
        }

    }

    private fun getTextOtp(): String {
        val otp = StringBuilder()
        for (editText in editTexts) {
            otp.append(editText.text.toString())
        }

        return otp.toString()
    }
}
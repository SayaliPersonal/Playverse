package com.robosoft.playverse.feature.presentation.view.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.robosoft.playverse.R
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.robosoft.playverse.databinding.FragmentReferralCodeBinding
import kotlinx.android.synthetic.main.custom_toast.*

class ReferralCodeFragment(private var referralCodeListener: ReferralCodeListener) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentReferralCodeBinding

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


        }

        override fun beforeTextChanged(
            s: CharSequence, start: Int, count: Int,
            after: Int
        ) {

        }

        override fun afterTextChanged(s: Editable) {
            if (s.length == 5) {
                binding.ivReferralCodeValid.setImageResource(R.drawable.login_mobile_verify_tick)
                referralCodeListener.getToastMsg(true)
            } else {
                binding.ivReferralCodeValid.setImageResource(R.drawable.ic_error)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReferralCodeBinding.inflate(inflater, container, false)

        binding.btnReferral.setOnClickListener {
            dismiss()
            referralCodeListener.getReferralCode("10", true)
        }

        binding.etReferralCode.addTextChangedListener(mTextEditorWatcher)

        return binding.root
    }
}
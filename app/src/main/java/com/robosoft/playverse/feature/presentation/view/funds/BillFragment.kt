package com.robosoft.playverse.feature.presentation.view.funds

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FragmentBillBinding
import com.robosoft.playverse.databinding.FragmentLoginBinding
import com.robosoft.playverse.databinding.FragmentSelectPaymentModeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBillBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillBinding.inflate(inflater,container,false)
        return binding.root
    }


}
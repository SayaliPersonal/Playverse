package com.robosoft.playverse.feature.presentation.view.funds

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FragmentSelectPaymentModeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectPaymentModeFragment : Fragment() {
    private lateinit var binding: FragmentSelectPaymentModeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectPaymentModeBinding.inflate(inflater,container,false)
        return binding.root
    }

}
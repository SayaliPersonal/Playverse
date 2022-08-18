package com.robosoft.playverse.feature.presentation.view.funds

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FragmentProcessingRequestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProcessingRequestFragment : Fragment() {
    private lateinit var binding : FragmentProcessingRequestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProcessingRequestBinding.inflate(inflater,container,false)
        return binding.root
    }

}
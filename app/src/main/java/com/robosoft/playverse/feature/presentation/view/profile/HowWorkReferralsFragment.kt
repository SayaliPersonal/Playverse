package com.robosoft.playverse.feature.presentation.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.robosoft.playverse.databinding.FragmentHowWorkReferralsBinding

class HowWorkReferralsFragment : Fragment() {

    private lateinit var binding: FragmentHowWorkReferralsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHowWorkReferralsBinding.inflate(inflater, container, false)

        binding.ivBack.setOnClickListener {
            val action =
                HowWorkReferralsFragmentDirections.actionHowWorkReferralsFragmentToProfileDetailsFragment(
                )
            findNavController().navigate(action)
        }

        return binding.root
    }

}
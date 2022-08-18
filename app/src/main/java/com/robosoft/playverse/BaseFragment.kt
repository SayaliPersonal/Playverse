package com.robosoft.playverse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.databinding.FragmentBaseBinding
import com.robosoft.playverse.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.header_footer.view.*
import javax.inject.Inject

@AndroidEntryPoint
class BaseFragment : Fragment() {

    private lateinit var binding: FragmentBaseBinding
    @Inject
    lateinit var appStorage: AppStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseBinding.inflate(inflater, container, false)
        bindUI()
        return binding.root
    }

    private fun bindUI() {
            if (appStorage.userId.isEmpty()) {
                findNavController().navigate(R.id.action_baseFragment_to_getStartedFragment)
            } else {
                findNavController().navigate(R.id.action_baseFragment_to_homeFragment)
            }

    }
}
package com.robosoft.playverse.feature.presentation.view.linkUPI

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FragmentUpiLinkBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpiLinkFragment : Fragment() {

    private lateinit var binding: FragmentUpiLinkBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpiLinkBinding.inflate(inflater,container,false)
        bindUI()
        return binding.root
    }

    private fun bindUI() {

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        /*val blurEffect = RenderEffect.createBlurEffect(10F, 10F, Shader.TileMode.CLAMP)
        binding.card.setRenderEffect(blurEffect)
        binding.textView5.setRenderEffect(blurEffect)*/

    }

}
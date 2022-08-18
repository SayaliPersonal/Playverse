package com.robosoft.playverse.feature.presentation.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.playverse.data.models.AvatarResponse
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.databinding.FragmentSelectAvatarBinding
import com.robosoft.playverse.feature.presentation.viewModel.EditProfileViewModel
import com.robosoft.playverse.feature.presentation.viewModel.LoginViewModel
import com.robosoft.playverse.utilities.TemporaryStorage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectAvatarFragment : Fragment() {
    private lateinit var binding: FragmentSelectAvatarBinding

    private val viewModel : EditProfileViewModel by viewModels()

    private val args : SelectAvatarFragmentArgs by navArgs()

    private val loginViewModel : LoginViewModel by viewModels()

    @Inject
    lateinit var appStorage : AppStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentSelectAvatarBinding.inflate(inflater, container, false)
        bindUI()
        return binding.root
    }

    private fun bindUI() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.ivAvatarOne.setOnClickListener {
            binding.bgAvatarOne.isVisible = true
            binding.bgAvatarTwo.isInvisible = true
            binding.bgAvatarThree.isInvisible = true
            binding.bgAvatarFour.isInvisible = true
            binding.ivAvatarOne.alpha = 1F
            binding.ivAvatarTwo.alpha = 0.4F
            binding.ivAvatarThree.alpha = 0.4F
            binding.ivAvatarFour.alpha = 0.4F
            viewModel.saveProfile(R.drawable.avatar_one)
            binding.selected = true
        }

        binding.ivAvatarTwo.setOnClickListener {
            binding.bgAvatarOne.isInvisible = true
            binding.bgAvatarTwo.isVisible = true
            binding.bgAvatarThree.isInvisible = true
            binding.bgAvatarFour.isInvisible = true
            binding.ivAvatarTwo.alpha = 1F
            binding.ivAvatarOne.alpha = 0.4F
            binding.ivAvatarThree.alpha = 0.4F
            binding.ivAvatarFour.alpha = 0.4F
            viewModel.saveProfile(R.drawable.avatar_two)
            binding.selected = true
        }

        binding.ivAvatarThree.setOnClickListener {
            binding.bgAvatarOne.isInvisible = true
            binding.bgAvatarTwo.isInvisible = true
            binding.bgAvatarThree.isVisible = true
            binding.bgAvatarFour.isInvisible = true
            binding.ivAvatarThree.alpha = 1F
            binding.ivAvatarTwo.alpha = 0.4F
            binding.ivAvatarOne.alpha = 0.4F
            binding.ivAvatarFour.alpha = 0.4F
            viewModel.saveProfile(R.drawable.avatar_three)
            binding.selected = true
        }

        binding.ivAvatarFour.setOnClickListener {
            binding.bgAvatarOne.isInvisible = true
            binding.bgAvatarTwo.isInvisible = true
            binding.bgAvatarThree.isInvisible = true
            binding.bgAvatarFour.isVisible = true
            binding.ivAvatarFour.alpha = 1F
            binding.ivAvatarTwo.alpha = 0.4F
            binding.ivAvatarThree.alpha = 0.4F
            binding.ivAvatarOne.alpha = 0.4F
            viewModel.saveProfile(R.drawable.avatar_four)
            binding.selected = true
        }

        binding.btnConfirmAvatar.setOnClickListener {
            val data: LiveData<AvatarResponse.MainResponse> = loginViewModel.updateProfilePic(
                TemporaryStorage.image!!  , appStorage.userId.toInt()
            )

            data.observe(viewLifecycleOwner){
                if(it.success){
                    if(args.fromProfileCreate) findNavController().navigate(R.id.profileCreateFragment)
                    else findNavController().navigate(R.id.profileEditFragment)
                }
            }
        }



    }



}
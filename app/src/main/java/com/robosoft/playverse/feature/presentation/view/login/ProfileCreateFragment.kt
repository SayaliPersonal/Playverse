package com.robosoft.playverse.feature.presentation.view.login

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.playverse.data.models.AvatarResponse
import com.playverse.data.util.Constants
import com.robosoft.playverse.BuildConfig
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.databinding.FragmentProfileCreateBinding
import com.robosoft.playverse.feature.presentation.viewModel.LoginViewModel
import com.robosoft.playverse.feature.presentation.viewModel.ProfileViewModel
import com.robosoft.playverse.utilities.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.NonCancellable.cancel
import okhttp3.MultipartBody
import java.io.File
import java.util.regex.Pattern
import javax.inject.Inject


/** ProfileCreateFragment class
 */
@AndroidEntryPoint
class ProfileCreateFragment : Fragment() {
    private lateinit var binding: FragmentProfileCreateBinding

    @Inject
    lateinit var appStorage: AppStorage

    private val profileViewModel : ProfileViewModel by viewModels()


    private val viewModel : LoginViewModel by viewModels()



    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(userName: Editable?) {
            val reg = "^[a-zA-Z].*"
            val PATTERN: Pattern = Pattern.compile(reg)
            val validUsername = PATTERN.matcher(userName).find()
            binding.validUserName = validUsername
            if(userName?.contains(" ") == true){
                binding.tvErrorMsg.isVisible = true
                binding.validUserName = false
            }else if(userName?.isEmpty() == true){
                binding.tvErrorMsg.isVisible = false
                binding.ivError.setImageResource(android.R.color.transparent)
            }
            else if(validUsername){
                binding.ivError.setImageResource(R.drawable.login_mobile_verify_tick)
                binding.tvErrorMsg.isVisible = false
            }else{
                binding.ivError.setImageResource(R.drawable.ic_error)
                binding.tvErrorMsg.isVisible = true
                binding.tvErrorMsg.text = getString(R.string.username_must_start_with_character)
            }

            binding.btnConfirmUsername.setOnClickListener {
                validateUserName(userName.toString())
            }


        }

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentProfileCreateBinding.inflate(inflater,container,false)
        bindUI()
        subscribeUI()
        return binding.root
    }

    private fun subscribeUI() {

        viewModel.profilePicUri.observe(viewLifecycleOwner){
            Glide.with(this)
                .load(it)
                .centerCrop()
                .into(binding.ivProfilePicture)
            viewModel.saveProfile()
        }


        profileViewModel.fetch.observe(viewLifecycleOwner) {
            if(it.data.url != Constants.DEFAULT_PROFILE_PIC) {
                Glide.with(this)
                    .load(it.data.url)
                    .centerCrop()
                    .into(binding.ivProfilePicture)
                binding.ivAvatar.isInvisible = true
                appStorage.profilePic = it.data.url
            }else{
                debugLog("invisible")
            }
        }

    }

    private fun bindUI() {
        binding.ivClose.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        TemporaryStorage.networkError.value = Connectivity.isNetworkAvailable(requireContext())
        if(TemporaryStorage.networkError.value == false){
            binding.networkErrorValue = true
        }

        binding.networkError.btTryAgain.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        binding.etUsername.addTextChangedListener(mTextEditorWatcher)
        binding.ivEdit.setOnClickListener {
            val action = ProfileCreateFragmentDirections.actionProfileCreateFragmentToProfileOptionsFragment(true)
            findNavController().navigate(action)
        }

        binding.ivProfilePicture.setOnClickListener {
            val action = ProfileCreateFragmentDirections.actionProfileCreateFragmentToProfileOptionsFragment(true)
            findNavController().navigate(action)
        }
    }

    private fun validateUserName(userName: String?) {
        val data: LiveData<AvatarResponse.MainResponse> = viewModel.updateProfileName(
            appStorage.userId.toInt(), userName ?: "")
        data.observe(viewLifecycleOwner){
            if(it?.success == true){
                appStorage.profilePic =  TemporaryStorage.image.toString()
                TemporaryStorage.networkError.value = Connectivity.isNetworkAvailable(requireContext())
                if(TemporaryStorage.networkError.value == false){
                    binding.networkErrorValue = true
                }else {
                    findNavController().navigate(R.id.action_profileCreateFragment_to_homeFragment)
                }
            }
        }


        viewModel.failureUsername.observe(viewLifecycleOwner){
            if(it.errorCode==404 || it.errorCode==500 || it.errorCode==400){
                binding.tvErrorMsg.isVisible = true
                binding.tvErrorMsg.text = getString(R.string.username_not_available, userName)
                binding.ivError.setImageResource(R.drawable.ic_error)
            }
        }


    }


}
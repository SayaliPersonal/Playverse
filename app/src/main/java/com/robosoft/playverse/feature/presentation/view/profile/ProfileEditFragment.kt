package com.robosoft.playverse.feature.presentation.view.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.playverse.data.models.AvatarResponse
import com.playverse.data.util.Constants
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.databinding.FragmentProfileEditBinding
import com.robosoft.playverse.feature.presentation.viewModel.LoginViewModel
import com.robosoft.playverse.feature.presentation.viewModel.ProfileViewModel
import com.robosoft.playverse.utilities.Connectivity
import com.robosoft.playverse.utilities.TemporaryStorage
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class ProfileEditFragment : Fragment() {
    private lateinit var binding: FragmentProfileEditBinding

    private val viewModel: LoginViewModel by viewModels()
    private val profileViewModel : ProfileViewModel by viewModels()

    @Inject
    lateinit var appStorage: AppStorage

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(userName: Editable?) {
            viewModel.saveProfile()
            val reg = "^[a-zA-Z].*"
            val PATTERN: Pattern = Pattern.compile(reg)
            val validUsername = PATTERN.matcher(userName).find()
            binding.layout.validUserName = validUsername
            if (userName?.contains(" ") == true) {
                binding.layout.tvErrorMsg.isVisible = true
                binding.layout.validUserName = false
            } else if(userName?.isEmpty() == true){
                binding.layout.tvErrorMsg.isInvisible = true
                binding.layout.ivError.setImageResource(android.R.color.transparent)
            } else if (validUsername) {
                binding.layout.ivError.setImageResource(R.drawable.login_mobile_verify_tick)
                binding.layout.tvErrorMsg.isVisible = false
            } else{
                binding.layout.ivError.setImageResource(R.drawable.ic_error)
                binding.layout.tvErrorMsg.isVisible = true
                binding.layout.tvErrorMsg.text = getString(R.string.username_must_start_with_character)
            }

            binding.layout.btnConfirmUsername.setOnClickListener {
                validateUserName(userName.toString())
            }


        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        bindUI()
        subscribeUI()
        return binding.root
    }

    private fun subscribeUI() {
        profileViewModel.fetch.observe(viewLifecycleOwner) {
            if(it.data.url != Constants.DEFAULT_PROFILE_PIC) {
                Glide.with(this)
                    .load(it.data.url)
                    .centerCrop()
                    .into(binding.layout.ivProfilePicture)
                binding.layout.ivAvatar.isInvisible = true
            }
            binding.layout.progressBar.isGone=true
            appStorage.profilePic = it.data.url
            if (it.data.name == "") {
                val phoneNo = appStorage.phoneNo
                val userName = phoneNo.substring(0, 3) + "xxxx" + phoneNo.substring(7, 10)
                binding.layout.etUsername.hint = userName

            } else {
                binding.layout.etUsername.hint = it.data.name

            }
        }
    }


    private fun bindUI() {
        binding.layout.progressBar.isVisible=true
        binding.layout.tvCreateProfile.text = getString(R.string.edit_profile)
        binding.layout.tvSetUsername.text = getString(R.string.edit_username_and_avatar)
        binding.layout.tvChooseUserName.isGone = true

        binding.layout.ivClose.setOnClickListener {
            findNavController().navigate(R.id.profileDetailsFragment)
        }

        binding.layout.ivEdit.setOnClickListener {
            findNavController().navigate(R.id.profileOptionsFragment)
        }

        binding.layout.ivProfilePicture.setOnClickListener {
            findNavController().navigate(R.id.profileOptionsFragment)
        }

        binding.layout.etUsername.addTextChangedListener(mTextEditorWatcher).also {
            binding.layout.tvBg.isEnabled = true
        }
    }

    private fun validateUserName(userName: String?) {
        val data: LiveData<AvatarResponse.MainResponse> = viewModel.updateProfileName(
             appStorage.userId.toInt(), userName ?: ""
        )

        data.observe(viewLifecycleOwner) {
            if (it?.code == 200) {
                binding.layout.tvErrorMsg.isInvisible = true
                binding.layout.ivError.setImageResource(android.R.color.transparent)
                binding.layout.tvUsernameUpdated.isVisible = true
                binding.layout.tvUsernameUpdated.postDelayed(
                    Runnable { binding.layout.tvUsernameUpdated.visibility = View.INVISIBLE },
                    2000
                )
                TemporaryStorage.networkError.value =
                    Connectivity.isNetworkAvailable(requireContext())
                if (TemporaryStorage.networkError.value == false) {
                    binding.layout.networkErrorValue = true
                }
            }
        }


        viewModel.failureUsername.observe(viewLifecycleOwner) {
            if (it.errorCode == 404 || it.errorCode == 500 || it.errorCode == 400) {
                binding.layout.tvUsernameUpdated.isVisible = false
                binding.layout.tvErrorMsg.isVisible = true
                binding.layout.tvErrorMsg.text =
                    getString(R.string.username_not_available, userName)
                binding.layout.ivError.setImageResource(R.drawable.ic_error)
            }
        }


    }

    override fun onResume() {
        super.onResume()
        if (view == null) {
            return
        }
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                findNavController().navigate(R.id.profileDetailsFragment)
                true
            } else false
        }
    }


}
package com.robosoft.playverse.feature.presentation.view.login

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.gson.Gson
import com.playverse.data.models.MainOtpResponse
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.databinding.FragmentLoginOtpBinding
import com.robosoft.playverse.feature.presentation.viewModel.LoginViewModel
import com.robosoft.playverse.feature.presentation.viewModel.ProfileViewModel
import com.robosoft.playverse.utilities.Connectivity
import com.robosoft.playverse.utilities.OtpManager
import com.robosoft.playverse.utilities.TemporaryStorage
import com.robosoft.playverse.utilities.debugLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


/**LoginOtpFragment Class*/
@AndroidEntryPoint
class LoginOtpFragment : Fragment() {


    private lateinit var binding: FragmentLoginOtpBinding

    private val profileViewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var appStorage: AppStorage

    private val args: LoginOtpFragmentArgs by navArgs()

    private val viewModel: LoginViewModel by viewModels()
    private var timer = Timer()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginOtpBinding.inflate(inflater, container, false)
        bindUI()
        /* subscribeUI()*/
        /*requestPermissions()*/
        return binding.root
    }

    private fun subscribeUI() {
        val client = SmsRetriever.getClient(requireContext())
        client.startSmsUserConsent(null)
    }

    private fun bindUI() {


        binding.ivClose.setOnClickListener {
            findNavController().navigate(R.id.action_loginOtpFragment_to_homeFragment)
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvMobileNo.text = getString(R.string.phone_no_with_country_code, args.phoneNo)

        manageOtp()

        TemporaryStorage.networkError.value = Connectivity.isNetworkAvailable(requireContext())
        if (TemporaryStorage.networkError.value == false) {
            binding.networkErrorValue = true
        }

        binding.networkError.btTryAgain.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

    }

    private fun manageOtp() {
        startTimer()

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.RECEIVE_SMS
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            TemporaryStorage.check.observe(viewLifecycleOwner) {
                binding.etNum1.setText(TemporaryStorage.check.value?.substring(0))
                binding.etNum2.setText(TemporaryStorage.check.value?.substring(1))
                binding.etNum3.setText(TemporaryStorage.check.value?.substring(2))
                binding.etNum4.setText(TemporaryStorage.check.value?.substring(3))
                binding.etNum5.setText(TemporaryStorage.check.value?.substring(4))
                binding.etNum6.setText(TemporaryStorage.check.value?.substring(5))
            }
        }

        OtpManager(
            InputType.TYPE_CLASS_NUMBER,
            binding.etNum1,
            binding.etNum2,
            binding.etNum3,
            binding.etNum4,
            binding.etNum5,
            binding.etNum6
        ) { otp ->
            verifyOtp(otp)
        }
    }

    private fun verifyOtp(otp: String) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000L)
        }
        /*if (otp.length == 6) findNavController().navigate(R.id.action_loginOtpFragment_to_profileCreateFragment)*/
        if (otp.length == 6) {

            val data: LiveData<MainOtpResponse> =
                viewModel.verifyOtpNew("91${args.phoneNo}", otp, TemporaryStorage.loginType)

            /**check failure condition*/
            viewModel.failure.observe(viewLifecycleOwner) {
                debugLog("status message is ${Gson().toJson(it.errorMsg)}")
                if (it.errorMsg == getString(R.string.max_limit_reached_for_otp_verification)) {
                    binding.tvErrorMsg.isVisible = true
                    binding.tvErrorMsg.text =
                        getString(R.string.max_limit_reached_for_otp_verification)
                    changeBackground()
                } else {
                    binding.tvErrorMsg.isVisible = true
                    binding.tvErrorMsg.text = getString(R.string.enter_correct_otp)
                    changeBackground()
                }
            }

            data.observe(viewLifecycleOwner) {
                when {
                    otp.length == 6 && it?.data?.type == "success" -> {
                        if (TemporaryStorage.loginType == "LOGIN") {
                            appStorage.userId = it.data.user_id
                            appStorage.accessToken = it.data.UserAuthResponse.accessToken
                            profileViewModel.fetch.observe(viewLifecycleOwner) {
                                appStorage.profilePic = it.data.url
                                findNavController().navigate(R.id.action_loginOtpFragment_to_homeFragment)
                            }

                        } else {
                            TemporaryStorage.networkError.value =
                                Connectivity.isNetworkAvailable(requireContext())
                            if (TemporaryStorage.networkError.value == false) {
                                binding.networkErrorValue = true
                            } else {
                                appStorage.userId = it.data.user_id
                                findNavController().navigate(R.id.action_loginOtpFragment_to_profileCreateFragment)
                            }
                        }
                    }
                    it.data.message == "OTP not match" -> {
                        binding.tvErrorMsg.isVisible = true
                        binding.tvErrorMsg.text = getString(R.string.enter_correct_otp)
                        changeBackground()
                    }
                }
            }
        }
    }

    private fun changeBackground() {
        binding.etNum1.setBackgroundResource(R.drawable.bg_error)
        binding.etNum2.setBackgroundResource(R.drawable.bg_error)
        binding.etNum3.setBackgroundResource(R.drawable.bg_error)
        binding.etNum4.setBackgroundResource(R.drawable.bg_error)
        binding.etNum5.setBackgroundResource(R.drawable.bg_error)
        binding.etNum6.setBackgroundResource(R.drawable.bg_error)

    }

    //Start Timer- 59 sec
    private fun startTimer() {
        binding.tvResend.isEnabled = false
        binding.tvResend.setTextColor(resources.getColor(R.color.white_opacity20))
        binding.tvTimer.setTextColor(resources.getColor(R.color.white))
        val timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val min: Long = millisUntilFinished / 1000 / 60
                val sec: Long = millisUntilFinished / 1000 % 60
                if (sec < 10) {
                    binding.tvTimer.setText("$min:0$sec")
                } else binding.tvTimer.setText("$min:$sec")

                if (sec == 0L) {
                    timer.cancel()
                    binding.tvResend.isEnabled = true
                    binding.tvResend.setTextColor(resources.getColor(R.color.white))
                    binding.tvTimer.setTextColor(resources.getColor(R.color.white_opacity20))
                    binding.tvResend.setOnClickListener {
                        getOtp(args.phoneNo)
                        binding.tvErrorMsg.isGone = true
                        changeEditTextBg()
                    }
                }


            }

            override fun onFinish() {
                timer.cancel()
            }
        }
        timer.start()
    }

    private fun changeEditTextBg() {
        binding.etNum1.setBackgroundResource(R.drawable.bg_edittext_login)
        binding.etNum2.setBackgroundResource(R.drawable.bg_edittext_login)
        binding.etNum3.setBackgroundResource(R.drawable.bg_edittext_login)
        binding.etNum4.setBackgroundResource(R.drawable.bg_edittext_login)
        binding.etNum5.setBackgroundResource(R.drawable.bg_edittext_login)
        binding.etNum6.setBackgroundResource(R.drawable.bg_edittext_login)
        binding.etNum1.setText("")
        binding.etNum2.setText("")
        binding.etNum3.setText("")
        binding.etNum4.setText("")
        binding.etNum5.setText("")
        binding.etNum6.setText("")


    }

    private fun getOtp(number: String) {
        val data: LiveData<MainOtpResponse> = viewModel.login("91$number")
        data.observe(viewLifecycleOwner) {
            manageOtp()
        }

    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.RECEIVE_SMS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    android.Manifest.permission.RECEIVE_SMS
                ), 100
            )
        }
    }
}
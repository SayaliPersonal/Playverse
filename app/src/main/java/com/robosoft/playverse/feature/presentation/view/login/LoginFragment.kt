package com.robosoft.playverse.feature.presentation.view.login

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.playverse.data.models.MainOtpResponse
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.databinding.FragmentLoginBinding
import com.robosoft.playverse.feature.presentation.viewModel.LoginViewModel
import com.robosoft.playverse.utilities.Connectivity
import com.robosoft.playverse.utilities.TemporaryStorage
import com.robosoft.playverse.utilities.debugLog
import com.robosoft.playverse.utilities.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.header_footer.*
import java.util.regex.Pattern
import javax.inject.Inject


/**LoginFragment Class*/
@AndroidEntryPoint
class LoginFragment : Fragment(), ReferralCodeListener {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var container: ViewGroup

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var appStorage: AppStorage

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


        }

        override fun beforeTextChanged(
            s: CharSequence, start: Int, count: Int,
            after: Int
        ) {

        }

        override fun afterTextChanged(s: Editable) {
            val reg = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}\$"
            val PATTERN: Pattern = Pattern.compile(reg)
            val phoneNoValid = PATTERN.matcher(s).find()
            binding.phoneNoValid = phoneNoValid
            if (phoneNoValid) {
                binding.ivPhoneNoValid.setImageResource(R.drawable.login_mobile_verify_tick)
                binding.tvValidPhoneNo.isVisible = false

                binding.btnLogin.setOnClickListener {
                    appStorage.phoneNo = s.toString()
                    binding.btnLogin.isEnabled = false
                    TemporaryStorage.networkError.value =
                        Connectivity.isNetworkAvailable(requireContext())
                    if (TemporaryStorage.networkError.value == false) {
                        binding.networkErrorValue = true
                    } else {
                        getOtp(s.toString())
                    }
                }
            } else {
                binding.ivPhoneNoValid.setImageResource(R.drawable.ic_error)
                binding.tvValidPhoneNo.isVisible = true
            }
        }
    }

    private fun getOtp(number: String) {
        val data: LiveData<MainOtpResponse> = viewModel.signUp("91$number")

        viewModel.loginFailure.observe(viewLifecycleOwner) {
            if (it.errorCode == 400) {
                debugLog("error_msg is ${it.errorMsg}")
                login(number)
            }
        }

        data.observe(viewLifecycleOwner) {
            if (it?.data?.type == "success") {
                binding.btnLogin.isEnabled = true
                TemporaryStorage.loginType = "SIGNUP"
                val action =
                    LoginFragmentDirections.actionLoginFragmentToLoginOtpFragment(number)
                findNavController().navigate(action)
            } else {
                binding.ivPhoneNoValid.setImageResource(R.drawable.ic_error)
                binding.tvValidPhoneNo.isVisible = true
            }
        }

    }

    private fun login(number: String) {
        val data: LiveData<MainOtpResponse> = viewModel.login("91$number")
        data.observe(viewLifecycleOwner) {
            if (it?.data?.type == "success") {
                binding.btnLogin.isEnabled = true
                TemporaryStorage.loginType = "LOGIN"
                val action =
                    LoginFragmentDirections.actionLoginFragmentToLoginOtpFragment(number)
                findNavController().navigate(action)
            } else {
                binding.ivPhoneNoValid.setImageResource(R.drawable.ic_error)
                binding.tvValidPhoneNo.isVisible = true
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        container?.let {
            this.container = container
        }
        bindUI()
        requestPermissions()
        return binding.root
    }

    private fun bindUI() {

        binding.tvValidPhoneNo.isVisible = false

        binding.etPhoneNo.addTextChangedListener(mTextEditorWatcher)

        binding.ivClose.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        binding.btnReferral.setOnClickListener {
            requireActivity().supportFragmentManager.let {
                ReferralCodeFragment(this).apply {
                    show(it, tag)
                }
            }
        }

        setUpTerms()

        TemporaryStorage.networkError.value = Connectivity.isNetworkAvailable(requireContext())
        if (TemporaryStorage.networkError.value == false) {
            binding.networkErrorValue = true
        }

        binding.networkError.btTryAgain.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        binding.ivCross.setOnClickListener {
            binding.tvRefrralCode.visible(false)
            binding.ivCross.visible(false)
            binding.btnReferral.text = getString(R.string.apply_referral_code)
        }
    }

    private fun setUpTerms() {
        val termsString = SpannableString(getString(R.string.terms_privacy_policy))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                findNavController().navigate(R.id.action_loginFragment_to_settingWebView)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.parseColor("#ffffff")
            }
        }
        val clickableSpanPrivacy = object : ClickableSpan() {
            override fun onClick(textView: View) {
                findNavController().navigate(R.id.action_loginFragment_to_settingWebView)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.parseColor("#ffffff")
            }
        }
        termsString.setSpan(clickableSpan, 71, 87, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        termsString.setSpan(clickableSpanPrivacy, 90, 104, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvTermsPrivacy.text = termsString
        binding.tvTermsPrivacy.movementMethod = LinkMovementMethod.getInstance()
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

    override fun getReferralCode(amount: String, isSuccess: Boolean) {
        binding.tvRefrralCode.visible(true)
        binding.ivCross.visible(true)
        binding.btnReferral.text = getString(R.string.change_referral_code)
    }

    override fun getToastMsg(isShow: Boolean) {
        if (isShow) {
            val inflater = layoutInflater
            val layout: View = inflater.inflate(
                R.layout.custom_toast, toast_layout_root
            )

            val toast = Toast(requireContext())
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
        }
    }
}
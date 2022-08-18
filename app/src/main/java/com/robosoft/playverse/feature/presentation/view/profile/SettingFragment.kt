package com.robosoft.playverse.feature.presentation.view.profile

import android.app.AlertDialog
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.playverse.data.util.Constants
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.databinding.FragmentSettingBinding
import com.robosoft.playverse.feature.presentation.viewModel.SettingViewModel
import com.robosoft.playverse.utilities.SessionClass
import com.robosoft.playverse.utilities.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.text_cross_button.view.*
import javax.inject.Inject


@AndroidEntryPoint
class SettingFragment : Fragment(), GetSettingListener {

    private lateinit var binding: FragmentSettingBinding
    private var list: ArrayList<SettingData> = ArrayList()
    private val settingViewModel: SettingViewModel by viewModels()


    @Inject
    lateinit var appStorage: AppStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        binding.settingHeader.tvGetStarted.text = getString(R.string.settings_app)
        binding.settingHeader.tvEnterNo.text = "v ${getCurrentVersion()} - D"

        list.clear()
        updateSettingList()

        binding.rankRecycle.layoutManager =
            LinearLayoutManager(requireContext())
        binding.rankRecycle.adapter = SettingAdapter(list = list, this)

        binding.settingHeader.ivClose.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_profileDetailsFragment)
        }

        return binding.root
    }

    private fun updateSettingList() {
        list.add(
            SettingData(
                getString(R.string.policy_privacy), ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_eye,
                    null
                )
            )
        )
        list.add(
            SettingData(
                getString(R.string.terms_and_condition), ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_terms,
                    null
                )
            )
        )
        list.add(
            SettingData(
                getString(R.string.logout), ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_logout,
                    null
                )
            )
        )
    }

    override fun getSettingItemPosition(position: Int, title: String) {
        if (position == 2) {
            val factory = LayoutInflater.from(context)
            val dialogView = factory.inflate(R.layout.custom_game_exit, null)
            val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
            val titleText = dialogView.findViewById<TextView>(R.id.textView23)
            val desText = dialogView.findViewById<TextView>(R.id.textView24)
            val btnQuit = dialogView.findViewById<Button>(R.id.btnQuit)
            titleText.text = getString(R.string.logout_)
            desText.text = getString(R.string.logout_des)
            btnCancel.text = getString(R.string.yes_logout)
            btnQuit.text = getString(R.string.cancel)

            val customDialog = AlertDialog.Builder(context, R.style.CustomAlertDialog)
                .setView(dialogView)
                .show()
            btnCancel.setOnClickListener {
                try {
                    /*logout api call*/
                    settingViewModel.logout(appStorage.accessToken)
                    settingViewModel.logoutResponse.observe(viewLifecycleOwner) {
                        if (it.success) {
                            SessionClass.getClear(requireContext(), "check")
                            appStorage.profilePic = Constants.DEFAULT_PROFILE_PIC
                            val navOptions = NavOptions.Builder()
                                .setPopUpTo(R.id.nav_main, true)
                                .build()
                            findNavController().navigate(R.id.getStartedFragment, null, navOptions)
                        } else {
                            toast("Something went wrong...!")
                        }
                    }
                    customDialog.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            btnQuit.setOnClickListener {
                try {
                    customDialog.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            customDialog.setCanceledOnTouchOutside(false)
        } else {
            findNavController().navigate(R.id.settingWebView)
        }
    }

    private fun getCurrentVersion(): String {
        var version = ""
        try {
            val pInfo: PackageInfo =
                requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return version
    }
}


data class SettingData(var title: String, var image: Drawable?)
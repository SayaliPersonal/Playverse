package com.robosoft.playverse.feature.presentation.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.playverse.data.util.Constants
import com.robosoft.playverse.R
import com.robosoft.playverse.base.AppStorage
import com.robosoft.playverse.feature.presentation.viewModel.ProfileViewModel
import com.robosoft.playverse.utilities.Connectivity
import com.robosoft.playverse.utilities.TemporaryStorage
import dagger.hilt.android.AndroidEntryPoint
import com.robosoft.playverse.databinding.FragmentProfileDetailsBinding
import kotlinx.android.synthetic.main.game_info.view.*
import javax.inject.Inject

/**ProfileDetailsFragment class*/
@AndroidEntryPoint
class ProfileDetailsFragment : Fragment() {
    private lateinit var binding: FragmentProfileDetailsBinding

    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var appStorage: AppStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileDetailsBinding.inflate(inflater, container, false)
        bindUI()
        subscribeUI()
        return binding.root
    }

    private fun bindUI() {
        binding.progressBar.isVisible = true
        binding.profileStats.img_player.setImageResource(R.drawable.ic_trophy)
        binding.profileStats.textView.text = "50"
        binding.profileStats.textView2.text = "124"
        binding.profileStats.textView3.text = "08"
        binding.profileStats.txt_playerTitle.text = getString(R.string.tournaments)
        binding.profileStats.txt_game_plays.text = getString(R.string.game_plays)
        binding.profileStats.txt_game_won.text = getString(R.string.total_rewards)

        TemporaryStorage.networkError.value = Connectivity.isNetworkAvailable(requireContext())
        if (TemporaryStorage.networkError.value == false) {
            binding.networkErrorValue = true
        }

        binding.networkError.btTryAgain.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        val time = ServerDateTime(requireActivity())
        time.fetch(object : ServerDateTime.CallBack {
            override fun onGetDateTime(date: String?, time: String?) {
                TemporaryStorage.date = date?.substring(0, 10) + " " + date?.substring(11, 19)
            }
        })

        binding.ivSettings.setOnClickListener {
            findNavController().navigate(R.id.settingFragment)
        }

        binding.ivEdit.setOnClickListener {
            findNavController().navigate(R.id.profileEditFragment)
        }

        binding.ivProfilePicture.setOnClickListener{
            findNavController().navigate(R.id.profileEditFragment)
        }

        binding.ivAvatar.setOnClickListener{
            findNavController().navigate(R.id.profileEditFragment)
        }

        binding.tvUserName.setOnClickListener{
            findNavController().navigate(R.id.profileEditFragment)
        }
    }

    private fun subscribeUI() {
        val adapter = ProfileDetailsTabAdapter(requireActivity())
        binding.vpTournaments.adapter = adapter
        binding.vpTournaments.isUserInputEnabled = false
        binding.vpTournaments.let {
            TabLayoutMediator(binding.tabTournaments, it) { tab, position ->
                if (position == 0) {
                    tab.text = getString(R.string.tournaments)

                } else if (position == 1) {
                    tab.text = getString(R.string.referrals)
                }
            }.attach()
        }

        for (i in 0 until binding.tabTournaments.tabCount) {
            val tab = (binding.tabTournaments.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(10, 10, 10, 10)
            tab.requestLayout()
        }

        viewModel.fetch.observe(viewLifecycleOwner) {
            if (it.data.name == "") {
                val phoneNo = appStorage.phoneNo
                val userName = phoneNo.substring(0, 3) + "xxxx" + phoneNo.substring(7, 10)
                binding.tvUserName.text = userName
            } else {
                binding.tvUserName.text = it.data.name
            }
            if(it.data.url != Constants.DEFAULT_PROFILE_PIC) {
                Glide.with(this)
                    .load(it.data.url)
                    .centerCrop()
                    .into(binding.ivProfilePicture)
                binding.ivAvatar.isInvisible= true
            }

            binding.progressBar.isGone= true
            appStorage.profilePic = it.data.url
        }


    }


}
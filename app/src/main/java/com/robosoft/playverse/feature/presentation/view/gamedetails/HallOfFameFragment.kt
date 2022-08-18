package com.robosoft.playverse.feature.presentation.view.gamedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FragmentHallOfFameBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_hall_of_fame.view.*


@AndroidEntryPoint
class HallOfFameFragment : Fragment() {
    private lateinit var binding: FragmentHallOfFameBinding

    private var mIsRestoredFromBackstack: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHallOfFameBinding.inflate(inflater, container, false)
        bindUI()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIsRestoredFromBackstack=false
    }

    private fun bindUI() {
        binding.layoutKing.tvTitle.text = getString(R.string.king_scores)
        binding.layoutReward.tvTitle.text = getString(R.string.rewards_maverick)
        binding.layoutKing.img_rewards.setImageResource(R.drawable.ic_pink_podium)
        binding.layoutReward.img_rewards.setImageResource(R.drawable.ic_yellow_podium)

        binding.layoutLord.btnViewTopHundred.setOnClickListener {
            val action = HallOfFameFragmentDirections.actionHallOfFameFragmentToHallOfFameLeaderboardFragment(getString(R.string.lord_of_gameplays), getString(R.string.gamers_with_maximum_number_off_gameplays_in_air_fortress))
            findNavController().navigate(action)
        }

        binding.layoutKing.btnViewTopHundred.setOnClickListener {
            val action = HallOfFameFragmentDirections.actionHallOfFameFragmentToHallOfFameLeaderboardFragment(getString(R.string.king_scores), getString(R.string.gamers_with_maximum_number_off_gameplays_in_air_fortress))
            findNavController().navigate(action)
        }

        binding.layoutReward.btnViewTopHundred.setOnClickListener {
            val action = HallOfFameFragmentDirections.actionHallOfFameFragmentToHallOfFameLeaderboardFragment(getString(R.string.rewards_maverick), getString(R.string.gamers_with_maximum_number_off_gameplays_in_air_fortress))
            findNavController().navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()

    }

}
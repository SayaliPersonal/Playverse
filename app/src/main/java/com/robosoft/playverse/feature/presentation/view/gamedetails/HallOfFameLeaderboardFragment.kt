package com.robosoft.playverse.feature.presentation.view.gamedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.robosoft.playverse.R
import com.robosoft.playverse.databinding.FragmentHallOfFameBindingImpl
import com.robosoft.playverse.databinding.FragmentHallOfFameLeaderboardBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_hall_of_fame.view.*
import kotlinx.android.synthetic.main.text_cross_button.view.*

@AndroidEntryPoint
class HallOfFameLeaderboardFragment : Fragment() {
   private lateinit var binding : FragmentHallOfFameLeaderboardBinding

   private val args : HallOfFameLeaderboardFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHallOfFameLeaderboardBinding.inflate(inflater,container,false)
        bindUI()
        return binding.root
    }

    private fun bindUI(){
        binding.header.tvGetStarted.text = args.title
        binding.header.tvEnterNo.text = args.des

        if(args.title== getString(R.string.king_scores)){
            binding.imgRewards.setImageResource(R.drawable.ic_pink_podium)
        }else if(args.title == getString(R.string.rewards_maverick)){
            binding.imgRewards.setImageResource(R.drawable.ic_yellow_podium)
        }

        binding.header.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}
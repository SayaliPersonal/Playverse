package com.robosoft.playverse.feature.presentation.view.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.robosoft.playverse.databinding.FragmentProfileReferralsBinding
import kotlinx.android.synthetic.main.referral_buttons.view.*
import kotlinx.android.synthetic.main.referrals_count.view.*
import kotlinx.android.synthetic.main.share_card.view.*

class ProfileReferralsFragment : Fragment() {

    private lateinit var binding: FragmentProfileReferralsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileReferralsBinding.inflate(inflater, container, false)

        binding.constraintReferralCount.rvRefferral.layoutManager =
            LinearLayoutManager(requireContext())

        binding.constraintReferralCount.rvRefferral.adapter =
            ProfileReferralAdapter()

        binding.constraintReferralCard.setOnClickListener {
            val action =
                ProfileReferralsFragmentDirections.actionProfileReferralsFragmentToHowWorkReferralsFragment2(
                )
            findNavController().navigate(action)
        }

        binding.constraintShareCard.tournament_constraint.btn_help.setOnClickListener {
            var sharingIntent: Intent = Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            var shareBody: String = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }

        binding.constraintShareCard.tournament_constraint.btn_back_home.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=123456789"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()

    }
}